# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a LibGDX-based maze game called "Stumble Home" with tile-based map rendering and sprite animation. The game uses an orthographic camera with a zoomed-in viewport to navigate through maze paths.

## Build and Run Commands

```bash
# Run the game (desktop)
./gradlew lwjgl3:run

# Build runnable JAR
./gradlew lwjgl3:jar
# Output: lwjgl3/build/libs/

# Clean build artifacts
./gradlew clean

# Run tests
./gradlew test
```

## Architecture

### Core Game Structure

The game follows LibGDX's Screen-based architecture:

1. **StumbleHome** (main Game class)
   - Manages shared resources: SpriteBatch, BitmapFont, OrthographicCamera
   - Sets up viewport: Uses `FitViewport` with aspect ratio calculation
   - Viewport dimensions: 19.2×12 world units (VIEWPORT_HEIGHT=12, width calculated by aspect ratio)
   - Handles screen transitions

2. **MainMenuScreen**
   - Simple title screen waiting for SPACE key to start game
   - Transitions to GameScreen when player presses space

3. **GameScreen** (main gameplay)
   - Implements the core game loop: `input()` → `logic()` → `draw()`
   - Manages player movement, animation, and camera following

### Coordinate System and Scaling

**Critical**: The game uses a specific coordinate system that must be understood:

- **World units**: 1 world unit = 16 pixels
- **TiledMap scale**: Uses `OrthogonalTiledMapRenderer` with scale 1/16f
- **Map tiles**: 16×16 pixels per tile
- **Map size**: 60 tiles wide × 30 tiles tall = 960×480 pixels = 60×30 world units
- **Player size**: 0.8 world units
- **Sprite frames**: 25×49 pixels rendered with aspect ratio 49/25 = 1.96

### Player Animation System

The animation system distinguishes between walking and standing:

**Walking animations**: 4-frame sequences for each direction
- Each frame stored in an `Animation<TextureRegion>`
- Frame duration: 0.1 seconds (10 FPS)
- Directions: walkLeft, walkRight, walkUp, walkDown

**Standing poses**: Single static TextureRegions (not animated)
- Separate pose for each direction: standLeft, standRight, standUp, standDown
- Selected based on `lastDirection` enum when player stops moving
- When `currentAnimation == null`, the system uses `currentStandingPose`

**Animation state management**:
- `input()`: Sets `currentAnimation` when moving, sets it to null when stopped
- `logic()`: Resets animation timer when animation changes, updates stateTime
- `draw()`: Checks if currentAnimation is null to decide between animated frame vs static pose

### Camera and Boundary System

**Two-layer boundary system**:

1. **Player boundaries** (clampPlayerPosition)
   - Restricts player sprite from walking off map edges
   - Range: (0, 0) to (mapWidth - playerSize, mapHeight - playerSize)
   - Applied after input processing

2. **Camera boundaries** (clampCamera)
   - Prevents camera from showing empty space beyond map
   - Camera center clamped to keep viewport within map bounds
   - X range: minCameraX (9.6) to maxCameraX (50.4)
   - Y range: minCameraY (6.0) to maxCameraY (24.0)
   - Calculated as: `halfViewportSize` to `mapSize - halfViewportSize`

**Camera following**:
- Camera centers on player position (playerX + playerSize/2, playerY + playerSize/2)
- Camera position then clamped to boundaries
- Updated every frame in `logic()`

### TiledMap Integration

- Map loaded from `assets/map.tmx` using TmxMapLoader
- Map properties read at runtime: width, height, tilewidth, tileheight
- Uses OrthogonalTiledMapRenderer for rendering
- World dimensions calculated: `tilesCount * tilePixelSize / 16f`

### Asset Management

Assets are stored in the `assets/` directory and automatically bundled:

- `character.png`: Sprite sheet with exact pixel coordinates for frames (25×49 pixels each)
- `map.tmx`: Tiled map file with tile layers
- `plains.png`, `fences.png`: Tileset images referenced by map.tmx
- Assets referenced by filename only (no path prefix) in code

**Important**: Asset filenames must match exactly what's referenced in code. LibGDX looks for assets in the internal file system using the filename directly.

## Key Implementation Patterns

### Rendering with Projection Matrix

Always set the projection matrix before drawing sprites:
```java
game.batch.setProjectionMatrix(game.camera.combined);
game.batch.begin();
// drawing code
game.batch.end();
```

### Sprite Aspect Ratio Preservation

When sprite frames have non-square dimensions, calculate aspect ratio:
```java
float aspectRatio = frameHeight / frameWidth;  // e.g., 49/25 = 1.96
float drawWidth = playerSize;
float drawHeight = playerSize * aspectRatio;
game.batch.draw(frame, x, y, drawWidth, drawHeight);
```

### Input Handling for 4-Direction Movement

Use `else if` chains to prevent diagonal movement:
```java
if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
    // move right
} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
    // move left
} else if (...)
```

### Screen Lifecycle

- Screens must implement LibGDX's `Screen` interface
- `show()`: Called when screen becomes active - set up input processors here
- `render(float delta)`: Called every frame
- `hide()`: Called when screen is hidden - clean up input processors here
- `dispose()`: Must manually dispose textures and resources (not automatically called)

## Debug Output

The game includes debug prints on startup showing:
- Map boundaries and dimensions
- Viewport size
- Camera position ranges
- Player starting position
- Animation initialization status

These are printed to stdout when the game launches and help verify the coordinate system setup.