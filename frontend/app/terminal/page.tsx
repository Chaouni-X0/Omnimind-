import React, { useState, useRef } from "react";
import { View, Text, TextInput, TouchableOpacity, ScrollView, StyleSheet } from "react-native";
import { useColors } from "../../hooks/useTheme";

export default function TerminalScreen() {
  const colors = useColors();
  const [input, setInput] = useState("");
  const [lines, setLines] = useState<any[]>([
    { id: "0", type: "output", content: "OmniMind Terminal v2.1.0" },
    { id: "1", type: "output", content: 'اكتب "help" للحصول على المساعدة' },
    { id: "2", type: "output", content: "" },
  ]);
  const scrollViewRef = useRef<ScrollView>(null);

  const handleCommand = () => {
    if (!input.trim()) return;
    const cmd = input.trim();
    let output: any[] = [];
    switch (cmd.toLowerCase()) {
      case "help": output = [{ id: `o-${Date.now()}`, type: "output", content: "help - عرض المساعدة\nclear - مسح الشاشة\nls - عرض الملفات\necho - طباعة نص\ndate - عرض التاريخ" }]; break;
      case "clear": setLines([]); setInput(""); return;
      case "ls": output = [{ id: `o-${Date.now()}`, type: "output", content: "src/  public/  package.json  tsconfig.json" }]; break;
      case "date": output = [{ id: `o-${Date.now()}`, type: "output", content: new Date().toISOString() }]; break;
      default: if (cmd.startsWith("echo ")) output = [{ id: `o-${Date.now()}`, type: "output", content: cmd.slice(5) }]; else output = [{ id: `o-${Date.now()}`, type: "error", content: `خطأ: أمر غير معروف "${cmd}"` }];
    }
    setLines((prev) => [...prev, { id: `i-${Date.now()}`, type: "input", content: `$ ${cmd}` }, ...output]);
    setInput("");
    setTimeout(() => scrollViewRef.current?.scrollToEnd({ animated: true }), 100);
  };

  const s = StyleSheet.create({
    container: { flex: 1, backgroundColor: "#0D0D12" },
    header: { flexDirection: "row", justifyContent: "space-between", alignItems: "center", paddingHorizontal: 20, paddingTop: 60, paddingBottom: 12, borderBottomWidth: 1, borderBottomColor: colors.border },
    headerTitle: { fontSize: 18, fontWeight: "700", color: "#00E5CC" },
    clearBtn: { paddingHorizontal: 12, paddingVertical: 6, borderRadius: 8, backgroundColor: colors.surfaceSecondary },
    clearText: { fontSize: 12, color: colors.muted, fontWeight: "600" },
    terminal: { flex: 1, paddingHorizontal: 16, paddingTop: 12 },
    line: { marginBottom: 4 },
    inputLine: { fontSize: 13, color: "#00E5CC", fontFamily: "monospace" },
    outputLine: { fontSize: 13, color: colors.foreground, fontFamily: "monospace" },
    errorLine: { fontSize: 13, color: "#FF4466", fontFamily: "monospace" },
    inputBar: { flexDirection: "row", alignItems: "center", paddingHorizontal: 16, paddingVertical: 12, borderTopWidth: 1, borderTopColor: colors.border, backgroundColor: "#0D0D12" },
    prompt: { fontSize: 13, color: "#00E5CC", fontFamily: "monospace", marginLeft: 8 },
    textInput: { flex: 1, fontSize: 13, color: colors.foreground, fontFamily: "monospace" },
  });

  return (
    <View style={s.container}>
      <View style={s.header}>
        <Text style={s.headerTitle}>Terminal</Text>
        <TouchableOpacity style={s.clearBtn} onPress={() => setLines([])}><Text style={s.clearText}>مسح</Text></TouchableOpacity>
      </View>
      <ScrollView ref={scrollViewRef} style={s.terminal} contentContainerStyle={{ paddingBottom: 8 }}>
        {lines.map((line) => (
          <View key={line.id} style={s.line}>
            <Text style={line.type === "input" ? s.inputLine : line.type === "error" ? s.errorLine : s.outputLine}>{line.content}</Text>
          </View>
        ))}
      </ScrollView>
      <View style={s.inputBar}>
        <Text style={s.prompt}>$</Text>
        <TextInput style={s.textInput} value={input} onChangeText={setInput} onSubmitEditing={handleCommand} returnKeyType="send" placeholder="اكتب أمر..." placeholderTextColor={colors.muted} />
      </View>
    </View>
  );
}
