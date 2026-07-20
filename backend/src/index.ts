import express, { Express, Request, Response } from 'express';
import cors from 'cors';
import dotenv from 'dotenv';
import { createServer } from 'http';
import { Server as SocketIOServer } from 'socket.io';
import terminalRoutes from './routes/terminalRoutes';
import editorRoutes from './routes/editorRoutes';
import githubRoutes from './routes/githubRoutes';
import chatRoutes from './routes/chatRoutes';
import { editorService } from './services/editorService';
import { chatService } from './services/chatService';

// Load environment variables
dotenv.config();

const app: Express = express();
const port = process.env.PORT || 3000;

// Middleware
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// HTTP Server with Socket.IO
const httpServer = createServer(app);
const io = new SocketIOServer(httpServer, {
  cors: {
    origin: process.env.EXPO_PUBLIC_API_URL || 'http://localhost:8081',
    methods: ['GET', 'POST'],
  },
});

// Health Check Route
app.get('/health', (req: Request, res: Response) => {
  res.json({
    status: 'ok',
    timestamp: new Date().toISOString(),
    version: '2.0.0',
  });
});

// Initialize services
editorService.initializeStorage().catch(console.error);

// API Routes
app.use('/api/terminal', terminalRoutes);
app.use('/api/editor', editorRoutes);
app.use('/api/github', githubRoutes);
app.use('/api/chat', chatRoutes);

// Projects Routes (placeholder)
app.get('/api/projects', (req: Request, res: Response) => {
  res.json({
    projects: [],
    message: 'Projects endpoint - to be implemented',
  });
});

app.post('/api/projects', (req: Request, res: Response) => {
  res.json({
    message: 'Create project endpoint - to be implemented',
    data: req.body,
  });
});

// Socket.IO Events
io.on('connection', (socket) => {
  console.log(`User connected: ${socket.id}`);

  socket.on('disconnect', () => {
    console.log(`User disconnected: ${socket.id}`);
  });

  // Terminal events
  socket.on('terminal:execute', (data) => {
    console.log(`Terminal command: ${data.command}`);
    // To be implemented
  });

  // Chat events
  socket.on('chat:join-room', (data) => {
    const { roomId, userId } = data;
    socket.join(`room:${roomId}`);
    console.log(`User ${userId} joined room ${roomId}`);
    
    // Notify others
    io.to(`room:${roomId}`).emit('chat:user-joined', {
      roomId,
      userId,
      timestamp: new Date().toISOString(),
    });
  });

  socket.on('chat:leave-room', (data) => {
    const { roomId, userId } = data;
    socket.leave(`room:${roomId}`);
    console.log(`User ${userId} left room ${roomId}`);
    
    // Notify others
    io.to(`room:${roomId}`).emit('chat:user-left', {
      roomId,
      userId,
      timestamp: new Date().toISOString(),
    });
  });

  socket.on('chat:message', (data) => {
    const { roomId, userId, userName, content, type } = data;
    console.log(`Chat message in ${roomId}: ${content}`);\n    \n    // Save message\n    const message = chatService.sendMessage(\n      roomId,\n      userId,\n      userName,\n      content,\n      type || 'text'\n    );\n\n    if (message) {\n      // Broadcast to room\n      io.to(`room:${roomId}`).emit('chat:message', message);\n    }\n  });\n\n  socket.on('chat:typing', (data) => {\n    const { roomId, userId, userName } = data;\n    io.to(`room:${roomId}`).emit('chat:typing', {\n      roomId,\n      userId,\n      userName,\n      timestamp: new Date().toISOString(),\n    });\n  });\n\n  socket.on('chat:stop-typing', (data) => {\n    const { roomId, userId } = data;\n    io.to(`room:${roomId}`).emit('chat:stop-typing', {\n      roomId,\n      userId,\n    });\n  });\n\n  // Editor events\n  socket.on('editor:change', (data) => {\n    console.log(`Editor change: ${data.file}`);\n    // Broadcast to all connected clients\n    io.emit('editor:change', data);\n  });\n});\n\n// Error handling middleware\napp.use((err: any, req: Request, res: Response, next: any) => {\n  console.error(err.stack);\n  res.status(500).json({\n    error: 'Internal Server Error',\n    message: err.message,\n  });\n});\n\n// 404 handler\napp.use((req: Request, res: Response) => {\n  res.status(404).json({\n    error: 'Not Found',\n    path: req.path,\n  });\n});\n\n// Start server\nhttpServer.listen(port, () => {\n  console.log(`🚀 OmniMind Backend Server running on http://localhost:${port}`);\n  console.log(`📡 Socket.IO server ready for real-time communication`);\n  console.log(`🔗 API Base URL: http://localhost:${port}/api`);\n});\n\n// Graceful shutdown\nprocess.on('SIGTERM', () => {\n  console.log('SIGTERM signal received: closing HTTP server');\n  httpServer.close(() => {\n    console.log('HTTP server closed');\n    process.exit(0);\n  });\n});\n\nexport default app;\n
