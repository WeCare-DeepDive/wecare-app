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
      'react-native-reanimated/plugin', // 이건 반드시 맨 마지막에!
    ],
  };
};
