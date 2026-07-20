package com.example.omnimind.domain.github

import com.example.omnimind.data.network.GitHubApiService
import com.example.omnimind.data.network.GitHubBranch
import com.example.omnimind.data.network.GitHubContentItem
import com.example.omnimind.data.network.GitHubRepo
import com.example.omnimind.data.network.GitHubUser

/**
 * غلاف حول GitHubApiService الحقيقي. يتطلب Personal Access Token
 * (Settings -> Developer settings -> Personal access tokens في GitHub).
 */
class GitHubService(private val api: GitHubApiService) {

    private fun authHeader(token: String) = "Bearer $token"

    suspend fun getUser(token: String): Result<GitHubUser> = runCatching {
        api.getAuthenticatedUser(authHeader(token))
    }

    suspend fun listMyRepositories(token: String): Result<List<GitHubRepo>> = runCatching {
        api.listMyRepos(authHeader(token))
    }

    suspend fun getRepositoryInfo(owner: String, repo: String, token: String): Result<GitHubRepo> = runCatching {
        api.getRepo(owner, repo, authHeader(token))
    }

    suspend fun listContents(
        owner: String,
        repo: String,
        path: String,
        token: String
    ): Result<List<GitHubContentItem>> = runCatching {
        api.getContents(owner, repo, path, authHeader(token))
    }

    suspend fun listBranches(owner: String, repo: String, token: String): Result<List<GitHubBranch>> = runCatching {
        api.getBranches(owner, repo, authHeader(token))
    }

    /** تنزيل محتوى ملف نصي عبر رابط download_url. */
    suspend fun downloadFileContent(downloadUrl: String): Result<String> = runCatching {
        api.downloadRaw(downloadUrl).string()
    }
}
