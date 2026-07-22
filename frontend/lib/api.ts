import axios, { AxiosInstance, AxiosError } from "axios";
import AsyncStorage from "@react-native-async-storage/async-storage";

const API_URL =
  process.env.EXPO_PUBLIC_API_URL || "http://localhost:3000";

class APIClient {
  private client: AxiosInstance;

  constructor() {
    this.client = axios.create({
      baseURL: API_URL,
      headers: {
        "Content-Type": "application/json",
      },
    });

    // Add request interceptor
    this.client.interceptors.request.use(async (config) => {
      const token = await AsyncStorage.getItem("authToken");
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      return config;
    });

    // Add response interceptor
    this.client.interceptors.response.use(
      (response) => response,
      (error: AxiosError) => {
        if (error.response?.status === 401) {
          AsyncStorage.removeItem("authToken");
        }
        return Promise.reject(error);
      }
    );
  }

  // Health check
  async health() {
    return this.client.get("/health");
  }

  // Terminal API
  terminal = {
    createSession: (cwd?: string) =>
      this.client.post("/api/terminal/sessions", { cwd }),
    execute: (sessionId: string, command: string) =>
      this.client.post("/api/terminal/execute", { sessionId, command }),
    getOutput: (sessionId: string) =>
      this.client.get(`/api/terminal/output/${sessionId}`),
    clearOutput: (sessionId: string) =>
      this.client.delete(`/api/terminal/output/${sessionId}`),
    closeSession: (sessionId: string) =>
      this.client.delete(`/api/terminal/sessions/${sessionId}`),
    getSessions: () => this.client.get("/api/terminal/sessions"),
  };

  // Editor API
  editor = {
    init: () => this.client.post("/api/editor/init"),
    listFiles: (path?: string) =>
      this.client.get("/api/editor/files", { params: { path } }),
    readFile: (path: string) =>
      this.client.get("/api/editor/read", { params: { path } }),
    writeFile: (path: string, content: string) =>
      this.client.post("/api/editor/write", { path, content }),
    createFile: (path: string, content?: string) =>
      this.client.post("/api/editor/create", { path, content }),
    deleteFile: (path: string) =>
      this.client.delete("/api/editor/delete", { params: { path } }),
    createDirectory: (path: string) =>
      this.client.post("/api/editor/mkdir", { path }),
    getSyntaxHighlighting: (path: string) =>
      this.client.get("/api/editor/syntax", { params: { path } }),
  };

  // GitHub API
  github = {
    auth: (accessToken: string) =>
      this.client.post("/api/github/auth", { accessToken }),
    getStatus: () => this.client.get("/api/github/status"),
    getRepos: () => this.client.get("/api/github/repos"),
    getRepo: (owner: string, repo: string) =>
      this.client.get(`/api/github/repos/${owner}/${repo}`),
    getBranches: (owner: string, repo: string) =>
      this.client.get(`/api/github/repos/${owner}/${repo}/branches`),
    getPullRequests: (owner: string, repo: string, state?: string) =>
      this.client.get(`/api/github/repos/${owner}/${repo}/pulls`, {
        params: { state },
      }),
    createPullRequest: (
      owner: string,
      repo: string,
      title: string,
      head: string,
      base: string,
      body?: string
    ) =>
      this.client.post(`/api/github/repos/${owner}/${repo}/pulls`, {
        title,
        head,
        base,
        body,
      }),
    getCommits: (owner: string, repo: string, branch?: string) =>
      this.client.get(`/api/github/repos/${owner}/${repo}/commits`, {
        params: { branch },
      }),
    getIssues: (owner: string, repo: string, state?: string) =>
      this.client.get(`/api/github/repos/${owner}/${repo}/issues`, {
        params: { state },
      }),
    logout: () => this.client.post("/api/github/logout"),
  };

  // Projects API
  projects = {
    getAll: () => this.client.get("/api/projects"),
    create: (data: any) => this.client.post("/api/projects", data),
  };

  // Chat API
  chat = {
    getMessages: (roomId: string) =>
      this.client.get(`/api/chat/rooms/${roomId}/messages`),
    sendMessage: (roomId: string, userId: string, userName: string, content: string) =>
      this.client.post("/api/chat/messages", { roomId, userId, userName, content }),
  };
}

export const api = new APIClient();
