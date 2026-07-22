import React, { useState } from "react";
import { View, Text, TextInput, TouchableOpacity, ScrollView, StyleSheet } from "react-native";
import { useColors } from "../../hooks/useTheme";

export default function GitHubScreen() {
  const colors = useColors();
  const [token, setToken] = useState("");
  const [connected, setConnected] = useState(false);

  const handleConnect = () => {
    if (token.trim().length > 10) setConnected(true);
  };

  const s = StyleSheet.create({
    container: { flex: 1, backgroundColor: colors.background },
    header: { paddingHorizontal: 20, paddingTop: 60, paddingBottom: 16, borderBottomWidth: 1, borderBottomColor: colors.border },
    headerTitle: { fontSize: 18, fontWeight: "700", color: colors.foreground },
    content: { flex: 1, paddingHorizontal: 20, paddingTop: 24 },
    statusCard: { backgroundColor: colors.surface, borderRadius: 16, padding: 20, borderWidth: 1, borderColor: colors.border, alignItems: "center", marginBottom: 24 },
    statusIcon: { fontSize: 48, marginBottom: 12 },
    statusText: { fontSize: 16, fontWeight: "600", color: connected ? colors.success : colors.muted },
    input: { backgroundColor: colors.surface, borderRadius: 12, paddingHorizontal: 16, paddingVertical: 14, fontSize: 15, color: colors.foreground, borderWidth: 1, borderColor: colors.border, marginBottom: 16 },
    connectBtn: { backgroundColor: connected ? "transparent" : colors.primary, borderRadius: 12, paddingVertical: 14, alignItems: "center", borderWidth: 1, borderColor: colors.primary },
    connectText: { color: connected ? colors.primary : "#0A0A0F", fontSize: 15, fontWeight: "700" },
    sectionTitle: { fontSize: 14, fontWeight: "600", color: colors.muted, marginBottom: 12, marginTop: 8 },
    repoItem: { flexDirection: "row", alignItems: "center", justifyContent: "space-between", paddingVertical: 14, borderBottomWidth: 1, borderBottomColor: colors.borderLight },
    repoName: { fontSize: 14, fontWeight: "600", color: colors.foreground },
    repoType: { fontSize: 11, color: colors.primary, backgroundColor: colors.primary + "15", paddingHorizontal: 8, paddingVertical: 3, borderRadius: 6 },
  });

  const repos = [{ name: "Omnimind-", type: "خاص" }, { name: "portfolio", type: "عام" }, { name: "api-server", type: "خاص" }];

  return (
    <View style={s.container}>
      <View style={s.header}><Text style={s.headerTitle}>GitHub</Text></View>
      <ScrollView style={s.content} contentContainerStyle={{ paddingBottom: 32 }}>
        <View style={s.statusCard}>
          <Text style={s.statusIcon}>{connected ? "✅" : "🔗"}</Text>
          <Text style={s.statusText}>{connected ? "متصل بـ GitHub" : "غير متصل"}</Text>
        </View>
        {!connected ? (
          <>
            <Text style={s.sectionTitle}>أدخل رمز الوصول (Token)</Text>
            <TextInput style={s.input} value={token} onChangeText={setToken} placeholder="ghp_xxxxxxxxxxxx" placeholderTextColor={colors.muted} secureTextEntry />
            <TouchableOpacity style={s.connectBtn} onPress={handleConnect}><Text style={s.connectText}>اتصال</Text></TouchableOpacity>
          </>
        ) : (
          <>
            <Text style={s.sectionTitle}>المستودعات</Text>
            {repos.map((repo, i) => (
              <TouchableOpacity key={i} style={s.repoItem}>
                <Text style={s.repoName}>{repo.name}</Text>
                <Text style={s.repoType}>{repo.type}</Text>
              </TouchableOpacity>
            ))}
            <TouchableOpacity style={[s.connectBtn, { marginTop: 24, borderColor: colors.error }]} onPress={() => setConnected(false)}>
              <Text style={[s.connectText, { color: colors.error }]}>قطع الاتصال</Text>
            </TouchableOpacity>
          </>
        )}
      </ScrollView>
    </View>
  );
}
