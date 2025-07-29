const { getDefaultConfig } = require('@expo/metro-config');
const path = require('path');

const projectRoot = __dirname;

const config = getDefaultConfig(projectRoot);

config.transformer = {
  ...config.transformer,
  babelTransformerPath: require.resolve('react-native-svg-transformer'),
};

config.resolver = {
  ...config.resolver,
  assetExts: config.resolver.assetExts.filter((ext) => ext !== 'svg'),
  sourceExts: [...config.resolver.sourceExts, 'svg'],
  resolverMainFields: ['react-native', 'browser', 'main'],

  // ✅ 여기가 중요: Metro alias
  extraNodeModules: {
    '@assets': path.resolve(__dirname, 'assets'),
    '@components': path.resolve(__dirname, 'src/components'),
    '@styles': path.resolve(__dirname, 'src/styles'),
    '@screen': path.resolve(__dirname, 'src/screen'),
  },
};

config.transformer.unstable_allowRequireContext = true;
// ✅ 이거 추가해보세요!
config.watchFolders = [path.resolve(__dirname, 'assets'), path.resolve(__dirname, 'src')];
module.exports = config;
