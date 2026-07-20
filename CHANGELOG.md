# Changelog

All notable changes to this project will be documented in this file.

## [1.1.0] - 2026-07-17

### Added
- Enhanced multi-language support with full RTL (Right-to-Left) support for Arabic
- Three premium themes: Obsidian (dark), Aurora (light), and Ember (warm dark)
- Advanced Inspector Panel with code editor, terminal logs, and context memory
- Quality Gate system with automated quality scorecards
- Budget management for API keys with unlimited option support
- Sandbox execution monitoring with real-time logs

### Fixed
- Fixed budget handling to properly support null values for unlimited usage
- Corrected Gemini model version from non-existent gemini-2.5-flash to stable gemini-1.5-flash
- Unified verdict field handling in agent response parsing (verdictType consistency)
- Fixed RTL text rendering issues in multiple screens
- Improved error handling in API key validation
- Fixed missing contentDescription in various icon buttons

### Changed
- Removed emoji from all UI elements for a more professional appearance
- Refactored SwarmEngine for better error handling and failover support
- Improved ViewModel state management with proper lifecycle handling
- Enhanced Translations system for comprehensive language support
- Updated all screens to follow Material Design 3 guidelines
- Improved API response parsing with better error recovery

### Improved
- Professional UI refinements across Dashboard, Settings, Profile, and AppBuilder screens
- Better visual hierarchy with improved spacing and typography
- Enhanced accessibility with proper contrast ratios (WCAG AA compliant)
- Optimized performance with efficient state management
- Better error messages and user feedback

## [1.0.0] - 2026-07-10

### Initial Release
- Multi-agent swarm intelligence system
- Jetpack Compose-based modern UI
- Room database for persistent storage
- Connector hub for multiple LLM providers
- Isolated sandbox environment
- Integrated code editor
- Project and task management
- Real-time agent communication
