import { EventEmitter } from 'events';
import { v4 as uuidv4 } from 'uuid';

export interface ChatMessage {
  id: string;
  roomId: string;
  userId: string;
  userName: string;
  userAvatar?: string;
  content: string;
  timestamp: Date;
  type: 'text' | 'code' | 'system' | 'command';
  language?: string; // For code messages
  metadata?: Record<string, any>;
}

export interface ChatRoom {
  id: string;
  name: string;
  description: string;
  createdAt: Date;
  updatedAt: Date;
  members: string[];
  messages: ChatMessage[];
  isArchived: boolean;
}

export class ChatService extends EventEmitter {
  private rooms: Map<string, ChatRoom> = new Map();
  private userSessions: Map<string, Set<string>> = new Map(); // userId -> Set of roomIds
  private maxMessagesPerRoom = 1000;

  /**
   * Create a new chat room
   */
  createRoom(name: string, description: string = ''): ChatRoom {
    const roomId = uuidv4();
    const room: ChatRoom = {
      id: roomId,
      name,
      description,
      createdAt: new Date(),
      updatedAt: new Date(),
      members: [],
      messages: [],
      isArchived: false,
    };

    this.rooms.set(roomId, room);
    this.emit('room:created', room);
    return room;
  }

  /**
   * Get a chat room
   */
  getRoom(roomId: string): ChatRoom | null {
    return this.rooms.get(roomId) || null;
  }

  /**
   * Get all chat rooms
   */
  getAllRooms(): ChatRoom[] {
    return Array.from(this.rooms.values()).filter((r) => !r.isArchived);
  }

  /**
   * Add a member to a room
   */
  addMember(roomId: string, userId: string): boolean {
    const room = this.rooms.get(roomId);
    if (!room) return false;

    if (!room.members.includes(userId)) {
      room.members.push(userId);

      // Track user sessions
      if (!this.userSessions.has(userId)) {
        this.userSessions.set(userId, new Set());
      }
      this.userSessions.get(userId)!.add(roomId);

      this.emit('member:joined', { roomId, userId });
    }

    return true;
  }

  /**
   * Remove a member from a room
   */
  removeMember(roomId: string, userId: string): boolean {
    const room = this.rooms.get(roomId);
    if (!room) return false;

    const index = room.members.indexOf(userId);
    if (index > -1) {
      room.members.splice(index, 1);
      this.userSessions.get(userId)?.delete(roomId);
      this.emit('member:left', { roomId, userId });\n    }\n\n    return true;\n  }\n\n  /**\n   * Send a message to a room\n   */\n  sendMessage(\n    roomId: string,\n    userId: string,\n    userName: string,\n    content: string,\n    type: 'text' | 'code' | 'system' | 'command' = 'text',\n    language?: string\n  ): ChatMessage | null {\n    const room = this.rooms.get(roomId);\n    if (!room) return null;\n\n    const message: ChatMessage = {\n      id: uuidv4(),\n      roomId,\n      userId,\n      userName,\n      content,\n      timestamp: new Date(),\n      type,\n      language,\n    };\n\n    room.messages.push(message);\n    room.updatedAt = new Date();\n\n    // Keep only last N messages\n    if (room.messages.length > this.maxMessagesPerRoom) {\n      room.messages = room.messages.slice(-this.maxMessagesPerRoom);\n    }\n\n    this.emit('message:sent', message);\n    return message;\n  }\n\n  /**\n   * Get messages from a room\n   */\n  getMessages(roomId: string, limit: number = 50): ChatMessage[] {\n    const room = this.rooms.get(roomId);\n    if (!room) return [];\n\n    return room.messages.slice(-limit);\n  }\n\n  /**\n   * Edit a message\n   */\n  editMessage(roomId: string, messageId: string, newContent: string): ChatMessage | null {\n    const room = this.rooms.get(roomId);\n    if (!room) return null;\n\n    const message = room.messages.find((m) => m.id === messageId);\n    if (!message) return null;\n\n    message.content = newContent;\n    this.emit('message:edited', message);\n    return message;\n  }\n\n  /**\n   * Delete a message\n   */\n  deleteMessage(roomId: string, messageId: string): boolean {\n    const room = this.rooms.get(roomId);\n    if (!room) return false;\n\n    const index = room.messages.findIndex((m) => m.id === messageId);\n    if (index > -1) {\n      const message = room.messages[index];\n      room.messages.splice(index, 1);\n      this.emit('message:deleted', { roomId, messageId });\n      return true;\n    }\n\n    return false;\n  }\n\n  /**\n   * Search messages in a room\n   */\n  searchMessages(roomId: string, query: string): ChatMessage[] {\n    const room = this.rooms.get(roomId);\n    if (!room) return [];\n\n    const lowerQuery = query.toLowerCase();\n    return room.messages.filter((m) =>\n      m.content.toLowerCase().includes(lowerQuery) ||\n      m.userName.toLowerCase().includes(lowerQuery)\n    );\n  }\n\n  /**\n   * Archive a room\n   */\n  archiveRoom(roomId: string): boolean {\n    const room = this.rooms.get(roomId);\n    if (!room) return false;\n\n    room.isArchived = true;\n    this.emit('room:archived', roomId);\n    return true;\n  }\n\n  /**\n   * Get user's rooms\n   */\n  getUserRooms(userId: string): ChatRoom[] {\n    const roomIds = this.userSessions.get(userId);\n    if (!roomIds) return [];\n\n    return Array.from(roomIds)\n      .map((id) => this.rooms.get(id))\n      .filter((room) => room && !room.isArchived) as ChatRoom[];\n  }\n\n  /**\n   * Get room statistics\n   */\n  getRoomStats(roomId: string) {\n    const room = this.rooms.get(roomId);\n    if (!room) return null;\n\n    return {\n      roomId,\n      name: room.name,\n      memberCount: room.members.length,\n      messageCount: room.messages.length,\n      createdAt: room.createdAt,\n      updatedAt: room.updatedAt,\n      lastMessage: room.messages[room.messages.length - 1] || null,\n    };\n  }\n}\n\nexport const chatService = new ChatService();\n
