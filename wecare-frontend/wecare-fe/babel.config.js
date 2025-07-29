module.exports = function (api) {
  api.cache(true);
  return {
    presets: ['babel-preset-expo'],
    plugins: [
      '@babel/plugin-transform-runtime',
      [
        'module-resolver',
        {
          alias: {
            '@assets': './assets',
            '@components': './src/components',
            '@styles': './src/styles',
          },
        },
      ],
      [
        'module:react-native-dotenv',
        {
          moduleName: '@env',
          path: '.env',
          blacklist: null,
          whitelist: null,
          safe: false,
          allowUndefined: true,
        },
      ],
      'react-native-reanimated/plugin', // 이건 반드시 맨 마지막에!
    ],
  };
};
