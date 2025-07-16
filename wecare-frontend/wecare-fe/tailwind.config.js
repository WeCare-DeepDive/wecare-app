/** @type {import('tailwindcss').Config} */
module.exports = {
  // NOTE: Update this to include the paths to all files that contain Nativewind classes.
  content: ['./App.js', './src/**/*.{js,jsx,ts,tsx}'],
  presets: [require('nativewind/preset')],
  theme: {
    extend: {
      colors: {
        // Primary
        'labels-primary': '#000000',
        'primary-90': '#001D6C',

        // 단색
        'custom-black': '#000000',
        'custom-white': '#ffffff',

        // purple scale
        'purple-500': '#7777FF',
        'purple-300': '#D6D8FF',
        'purple-600': '#352BFF',
        'purple-700': '#1600DD',
        'purple-200': '#EFF0FF',
        'purple-400': '#C6C9FC',

        // Gray Scale
        'gray-10': '#60656E',
        'gray-9': '#8A8D94',
        'gray-8': '#8E9198',
        'gray-7': '#B9BBBF',
        'gray-6': '#CDCFD2',
        'gray-5': '#D6D7D9',
        'gray-2': '#F5F6F7',
        'gray-1': '#F8F8F8',
        'gray-600': '#4B5563',

        // 버튼배경
        'icon-disable': '#374957',
      },
      fontFamily: {
        nanum: ['NanumSquareRoundOTF'],
        pretendard: ['Pretendard'],
      },
      fontSize: {
        18: '18px',
        20: '20px',
        24: '24px',
      },
      lineHeight: {
        16: '16px',
        22: '22px',
        32: '32px',
        38: '38px',
      },
      borderRadius: {
        24: '24px',
        190: '190px',
      },
      boxShadow: {
        header: '0 2px 4px rgba(0, 0, 0, 0.08)',
        nav: '0 -1px 5px rgba(0, 0, 0, 0.15)',
      },
    },
  },
  plugins: [],
};
