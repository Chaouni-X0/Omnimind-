import React, { useState } from "react";
import { View, Text, TextInput, TouchableOpacity, FlatList, StyleSheet } from "react-native";
import { useColors } from "../../hooks/useTheme";

const mockFiles = [
  { id: "1", name: "src", type: "directory" },
  { id: "2", name: "index.tsx", type: "file" },
  { id: "3", name: "app.tsx", type: "file" },
  { id: "4", name: "package.json", type: "file" },
  { id: "5", name: "tsconfig.json", type: "file" },
];

export default function EditorScreen() {
  const colors = useColors();
  const [code, setCode] = useState("import React from 'react';\nimport { View, Text } from 'react-native';\n\nexport default function App() {\n  return (\n    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>\n      <Text>Hello OmniMind!</Text>\n    </View>\n  );\n}");
  const [unsaved, setUnsaved] = useState(false);

  const s = StyleSheet.create({
    container: { flex: 1, backgroundColor: colors.background },
    header: { flexDirection: "row", justifyContent: "space-between", alignItems: "center", paddingHorizontal: 20, paddingTop: 60, paddingBottom: 16, borderBottomWidth: 1, borderBottomColor: colors.border },
    headerTitle: { fontSize: 18, fontWeight: "700", color: colors.foreground },
    saveBtn: { backgroundColor: unsaved ? colors.primary : "transparent", paddingHorizontal: 16, paddingVertical: 8, borderRadius: 10, borderWidth: 1, borderColor: colors.primary },
    saveText: { color: unsaved ? "#0A0A0F" : colors.muted, fontSize: 13, fontWeight: "600" },
    body: { flex: 1, flexDirection: "row" },
    fileList: { width: 180, borderRightWidth: 1, borderRightColor: colors.border, paddingVertical: 8 },
    fileItem: { flexDirection: "row", alignItems: "center", paddingVertical: 8, paddingHorizontal: 12 },
    fileText: { fontSize: 12, color: colors.muted, marginLeft: 8 },
    editorArea: { flex: 1, padding: 16 },
    lineNum: { fontSize: 13, color: colors.mutedLight, textAlign: "right", width: 30 },
    codeInput: { flex: 1, fontSize: 14, color: "#00E5CC", fontFamily: "monospace", lineHeight: 22, textAlignVertical: "top" },
    codeRow: { flexDirection: "row", flex: 1 },
    lineNums: { paddingTop: 4 },
  });

  return (
    <View style={s.container}>
      <View style={s.header}>
        <Text style={s.headerTitle}>المحرر</Text>
        <TouchableOpacity style={s.saveBtn} onPress={() => setUnsaved(false)}>
          <Text style={s.saveText}>{unsaved ? "حفظ" : "تم الحفظ"}</Text>
        </TouchableOpacity>
      </View>
      <View style={s.body}>
        <View style={s.fileList}>
          <FlatList data={mockFiles} keyExtractor={(item) => item.id} renderItem={({ item }) => (
            <View style={s.fileItem}>
              <Text>{item.type === "directory" ? "📁" : "📄"}</Text>
              <Text style={s.fileText}>{item.name}</Text>
            </View>
          )} />
        </View>
        <View style={s.editorArea}>
          <View style={s.codeRow}>
            <View style={s.lineNums}>
              {code.split("\n").map((_, i) => <Text key={i} style={s.lineNum}>{i + 1}</Text>)}
            </View>
            <TextInput style={s.codeInput} value={code} onChangeText={(t) => { setCode(t); setUnsaved(true); }} multiline editable />
          </View>
        </View>
      </View>
    </View>
  );
}
