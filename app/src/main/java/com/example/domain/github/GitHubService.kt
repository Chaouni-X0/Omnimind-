package com.example.omnimind.domain.github

import com.example.omnimind.data.network.GitHubApiService
import com.example.omnimind.data.network.GitHubContentItem
import com.example.omnimind.data.network.GitHubRepo
import com.example.omnimind.data.network.GitHubBranch
import com.example.omnimind.data.network.GitHubUser
import java.net.URI

/**
 * غلاف حول GitHubApiService الحقيقي. يتطلب Personal Access Token
 * (Settings -> Developer settings -> Personal access tokens في GitHub).
 */
class GitHubService(private val api: GitHubApiService) {

    private fun authHeader(token: String) = "Bearer $token"

    suspend fun getUser(token: String): Result<GitHubUser> = runCatching {
        api.getAuthenticatedUser(authHeader(token))
    }

    suspend fun listMyRepositories(token: String): Result<List<GitHubRepo>> {
        return try {
            Result.success(api.listMyRepos(authHeader(token)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRepositoryInfo(owner: String, repo: String, token: String): Result<GitHubRepo> {
        return try {
            Result.success(api.getRepo(owner, repo, authHeader(token)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun listContents(owner: String, repo: String, path: String, token: String): Result<List<GitHubContentItem>> {
        return try {
            Result.success(api.getContents(owner, repo, path, authHeader(token)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun listBranches(owner: String, repo: String, token: String): Result<List<GitHubBranch>> = runCatching {
        api.getBranches(owner, repo, authHeader(token))
    }

    suspend fun downloadFileContent(downloadUrl: String, token: String): Result<String> = runCatching {
        val host = URI(downloadUrl).host?.lowercase()
        require(host in ALLOWED_DOWNLOAD_HOSTS) { "رابط تنزيل GitHub غير موثوق" }
        api.downloadRaw(downloadUrl, authHeader(token)).string()
    }

    private companion object {
        val ALLOWED_DOWNLOAD_HOSTS = setOf(
            "api.github.com",
            "github.com",
            "raw.githubusercontent.com",
            "objects.githubusercontent.com"
        )
    }
}
