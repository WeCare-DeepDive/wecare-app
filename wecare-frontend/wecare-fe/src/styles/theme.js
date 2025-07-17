export const Colors = {
  // Primary
  labelsPrimary: '#000000',
  primary90: '#001D6C',

  // 단색
  customBlack: '#000000',
  customWhite: '#ffffff',

  // purple scale
  purple500: '#7777FF',
  purple300: '#D6D8FF',
  purple600: '#352BFF',
  purple700: '#1600DD',
  purple200: '#EFF0FF',
  purple400: '#C6C9FC',

  // Gray Scale
  gray10: '#60656E',
  gray9: '#8A8D94',
  gray8: '#8E9198',
  gray7: '#B9BBBF',
  gray6: '#CDCFD2',
  gray5: '#D6D7D9',
  gray2: '#F5F6F7',
  gray1: '#F8F8F8',
  gray600: '#4B5563',

  // 버튼배경
  iconDisable: '#374957',
};

export const FontFamily = {
  nanum: 'NanumSquareRoundOTF',
  pretendard: 'Pretendard',
};

export const FontSize = {
  18: 18,
  20: 20,
  24: 24,
};

export const LineHeight = {
  16: 16,
  22: 22,
  32: 32,
  38: 38,
};

export const BorderRadius = {
  24: 24,
  190: 190,
};

export const Shadows = {
  header: {
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.08,
    shadowRadius: 4,
    elevation: 3, // Android용
  },
  nav: {
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: -1,
    },
    shadowOpacity: 0.15,
    shadowRadius: 5,
    elevation: 5, // Android용
  },
};

export const Spacing = {
  p4: 4,
  p8: 8,
  p10: 10,
  p20: 20,
  gap8: 8,
};
/* Paddings */
export const Padding = {
  p_4: 4,
  p_8: 8,
  p_10: 10,
  p_20: 20,
  p_30: 30,
};
/* Gaps */
export const Gap = {
  gap_6: 6,
  gap_8: 8,
  gap_20: 20,
};

// 전체 테마 객체
export const Theme = {
  Colors,
  FontFamily,
  FontSize,
  LineHeight,
  BorderRadius,
  Shadows,
  Gap,
  Spacing,
  Padding,
};
