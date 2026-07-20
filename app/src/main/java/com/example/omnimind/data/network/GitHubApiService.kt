package com.example.omnimind.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Url

/**
 * واجهة حقيقية لجزء من GitHub REST API v3.
 * https://docs.github.com/en/rest
 */
interface GitHubApiService {
    @GET("user/repos?sort=updated&per_page=50")
    suspend fun listMyRepos(@Header("Authorization") auth: String): List<GitHubRepo>

    @GET("user")
    suspend fun getAuthenticatedUser(@Header("Authorization") auth: String): GitHubUser

    @GET("repos/{owner}/{repo}")
    suspend fun getRepo(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Header("Authorization") auth: String
    ): GitHubRepo

    @GET("repos/{owner}/{repo}/contents/{path}")
    suspend fun getContents(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String,
        @Header("Authorization") auth: String
    ): List<GitHubContentItem>

    @GET("repos/{owner}/{repo}/branches")
    suspend fun getBranches(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Header("Authorization") auth: String
    ): List<GitHubBranch>

    /** تنزيل المحتوى الخام لملف عبر رابط download_url مباشرة. */
    @GET
    suspend fun downloadRaw(@Url url: String): okhttp3.ResponseBody
}

data class GitHubUser(
    val login: String,
    val name: String?,
    val avatar_url: String?,
    val public_repos: Int = 0
)

data class GitHubBranch(
    val name: String
)

data class GitHubRepo(
    val id: Long,
    val name: String,
    val full_name: String,
    val private: Boolean,
    val html_url: String,
    val description: String?,
    val default_branch: String? = null,
    val stargazers_count: Int = 0,
    val language: String? = null
)

data class GitHubContentItem(
    val name: String,
    val path: String,
    val type: String, // "file" or "dir"
    val download_url: String?
)

