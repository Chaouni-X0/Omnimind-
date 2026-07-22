import React, { useState, useRef } from "react";
import { View, Text, TextInput, TouchableOpacity, ScrollView, StyleSheet } from "react-native";
import { useColors } from "../../hooks/useTheme";

export default function ChatScreen() {
  const colors = useColors();
  const [message, setMessage] = useState("");
  const [messages, setMessages] = useState<any[]>([
    { id: "1", content: "مرحباً! أنا OmniMind. كيف يمكنني مساعدتك؟", sender: "assistant", timestamp: Date.now() },
  ]);
  const scrollViewRef = useRef<ScrollView>(null);

  const handleSend = () => {
    if (!message.trim()) return;
    setMessages((prev: any[]) => [...prev, { id: `u-${Date.now()}`, content: message, sender: "user", timestamp: Date.now() }]);
    setMessage("");
    setTimeout(() => {
      setMessages((prev: any[]) => [...prev, { id: `a-${Date.now()}`, content: "شكراً! سأقوم بمعالجة طلبك...", sender: "assistant", timestamp: Date.now() }]);
      scrollViewRef.current?.scrollToEnd({ animated: true });
    }, 500);
  };

  const s = StyleSheet.create({
    container: { flex: 1, backgroundColor: colors.background },
    header: { paddingHorizontal: 20, paddingTop: 60, paddingBottom: 16, borderBottomWidth: 1, borderBottomColor: colors.border },
    headerTitle: { fontSize: 18, fontWeight: "700", color: colors.foreground },
    headerSub: { fontSize: 13, color: colors.muted, marginTop: 4 },
    messages: { flex: 1, paddingHorizontal: 16, paddingTop: 16 },
    msgRow: { flexDirection: "row", marginBottom: 12 },
    msgRowUser: { justifyContent: "flex-end" },
    msgRowAi: { justifyContent: "flex-start" },
    bubble: { maxWidth: "80%", paddingHorizontal: 16, paddingVertical: 12, borderRadius: 16 },
    bubbleUser: { backgroundColor: colors.primary, borderBottomRightRadius: 4 },
    bubbleAi: { backgroundColor: colors.surface, borderWidth: 1, borderColor: colors.border, borderBottomLeftRadius: 4 },
    msgText: { fontSize: 15, lineHeight: 22 },
    msgTextUser: { color: "#0A0A0F" },
    msgTextAi: { color: colors.foreground },
    inputBar: { flexDirection: "row", alignItems: "center", paddingHorizontal: 16, paddingVertical: 12, borderTopWidth: 1, borderTopColor: colors.border, gap: 10 },
    input: { flex: 1, backgroundColor: colors.surface, borderRadius: 24, paddingHorizontal: 18, paddingVertical: 12, fontSize: 15, color: colors.foreground, borderWidth: 1, borderColor: colors.border },
    sendBtn: { width: 44, height: 44, borderRadius: 22, backgroundColor: colors.primary, justifyContent: "center", alignItems: "center" },
    sendText: { color: "#0A0A0F", fontSize: 18, fontWeight: "700" },
  });

  return (
    <View style={s.container}>
      <View style={s.header}>
        <Text style={s.headerTitle}>الدردشة</Text>
        <Text style={s.headerSub}>OmniMind Assistant</Text>
      </View>
      <ScrollView ref={scrollViewRef} style={s.messages} contentContainerStyle={{ paddingBottom: 16 }}>
        {messages.map((msg) => (
          <View key={msg.id} style={[s.msgRow, msg.sender === "user" ? s.msgRowUser : s.msgRowAi]}>
            <View style={[s.bubble, msg.sender === "user" ? s.bubbleUser : s.bubbleAi]}>
              <Text style={[s.msgText, msg.sender === "user" ? s.msgTextUser : s.msgTextAi]}>{msg.content}</Text>
            </View>
          </View>
        ))}
      </ScrollView>
      <View style={s.inputBar}>
        <TextInput style={s.input} value={message} onChangeText={setMessage} placeholder="اكتب رسالتك..." placeholderTextColor={colors.muted} onSubmitEditing={handleSend} returnKeyType="send" />
        <TouchableOpacity style={s.sendBtn} onPress={handleSend}><Text style={s.sendText}>↑</Text></TouchableOpacity>
      </View>
    </View>
  );
}
