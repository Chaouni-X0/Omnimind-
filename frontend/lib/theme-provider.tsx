import React, { createContext, useContext } from "react";
import { useColorScheme as useSystemColorScheme } from "react-native";
import { Colors } from "../constants/theme";

type ColorScheme = "light" | "dark";

interface ThemeContextType {
  colorScheme: ColorScheme;
  colors: typeof Colors.light;
  setScheme: (scheme: ColorScheme) => void;
}

const ThemeContext = createContext<ThemeContextType>({
  colorScheme: "dark",
  colors: Colors.dark,
  setScheme: () => {},
});

export function ThemeProvider({ children }: { children: React.ReactNode }) {
  const systemScheme = useSystemColorScheme() ?? "dark";
  const [scheme, setScheme] = React.useState<ColorScheme>("dark");

  const colors = Colors[scheme];

  return (
    <ThemeContext.Provider
      value={{ colorScheme: scheme, colors, setScheme }}
    >
      {children}
    </ThemeContext.Provider>
  );
}

export function useThemeContext() {
  return useContext(ThemeContext);
}
