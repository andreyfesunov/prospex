# prospex

![Kotlin](https://img.shields.io/badge/kotlin-android-blue)

**prospex** is a Kotlin fat client application designed for local-first usage, helping users compare business ideas efficiently and privately on their own device. Ideal for entrepreneurs and innovators, prospex makes it simple to assess, score, and evaluate new business opportunities offline.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Documentation

See the [docs](docs/) folder for setup instructions, usage guides, and more information.

## Releases

### Creating a Release

The project uses GitHub Actions to automatically build and publish APK releases. There are two ways to create a release:

#### Method 1: Push a Tag

1. Create and push a tag with version number:
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```

2. The workflow will automatically:
   - Build the release APK
   - Create a GitHub release with the tag
   - Upload the APK to the release

#### Method 2: Manual Workflow Dispatch

1. Go to the **Actions** tab in GitHub
2. Select **Build and Release** workflow
3. Click **Run workflow**
4. Enter the version tag (e.g., `v1.0.0`)
5. Click **Run workflow**

The workflow will build the APK and create a release with the specified version.

### Download Releases

All releases are available in the [Releases](https://github.com/andreyfesunov/prospex/releases) section of the repository.