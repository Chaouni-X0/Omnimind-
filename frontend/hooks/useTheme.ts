import { useEffect, useState } from 'react';
import { ColorScheme, Colors, ThemeColorPalette } from '../constants/theme';

export function useTheme(colorSchemeOverride?: ColorScheme) {
  const [colorScheme, setColorScheme] = useState<ColorScheme>('light');

  useEffect(() => {
    // Detect system preference
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
    setColorScheme(prefersDark ? 'dark' : 'light');

    // Listen for changes
    const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
    const handleChange = (e: MediaQueryListEvent) => {
      setColorScheme(e.matches ? 'dark' : 'light');
    };

    mediaQuery.addEventListener('change', handleChange);
    return () => mediaQuery.removeEventListener('change', handleChange);
  }, []);

  const scheme = (colorSchemeOverride ?? colorScheme) as ColorScheme;
  return Colors[scheme];
}

export function useColors(colorSchemeOverride?: ColorScheme): ThemeColorPalette {
  return useTheme(colorSchemeOverride);
}

export function useColorScheme(): ColorScheme {
  const [colorScheme, setColorScheme] = useState<ColorScheme>('light');

  useEffect(() => {
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
    setColorScheme(prefersDark ? 'dark' : 'light');
  }, []);

  return colorScheme;
}
