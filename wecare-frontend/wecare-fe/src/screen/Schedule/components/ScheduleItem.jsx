// ScheduleItem.jsx
import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import IconSpoon from '@assets/Iconsvg/IconContainer/IconSpoon.svg';
import IconCapsule from '@assets/Iconsvg/IconContainer/IconCapsule.svg';
import IconFrame from '@assets/Iconsvg/IconContainer/IconFrame.svg';
import { FontFamily, Padding, Colors, FontSize, Gap, BorderRadius } from '../../../styles/theme';

const ScheduleItem = ({ title, time, icon }) => {
  const renderIcon = () => {
    switch (icon) {
      case 'IconSpoon':
        return <IconSpoon style={styles.iconspoon} width={20} height={24} />;
      case 'IconCapsule':
        return <IconCapsule style={styles.iconspoon} width={20} height={24} />;
      case 'IconFrame':
        return <IconFrame style={styles.iconspoon} width={20} height={24} />;
      default:
        return <IconSpoon style={styles.iconspoon} width={20} height={24} />;
    }
  };

  return (
    <View style={[styles.cardsallroutines, styles.cardsallroutinesLayout]}>
      <View style={[styles.routinecheck, styles.containerFlexBox]}>
        <View style={[styles.routine, styles.routineFlexBox]}>
          <View style={[styles.iconcontainer, styles.faviconFlexBox]}>{renderIcon()}</View>
          <View style={styles.alramtextcontainer}>
            <Text style={[styles.text41, styles.textTypo2]}>{title}</Text>
            <Text style={[styles.text42, styles.textTypo2]}>{time}</Text>
          </View>
        </View>
        <View style={[styles.buttonscheckboxWrapper, styles.routineFlexBox]}>
          <View style={[styles.buttonscheckbox, styles.sunLayout]} />
        </View>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  cardsallroutinesLayout: {
    width: 320,
    alignItems: 'flex-end',
  },
  containerFlexBox: {
    alignSelf: 'stretch',
    alignItems: 'center',
    flexDirection: 'row',
  },
  routineFlexBox: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  faviconFlexBox: {
    width: 48,
    justifyContent: 'center',
    alignItems: 'center',
    flexDirection: 'row',
  },
  textTypo2: {
    fontFamily: FontFamily.pretendardVariable,
    alignSelf: 'stretch',
    textAlign: 'left',
  },
  iconspoon: {
    width: 20,
    height: 24,
  },
  iconcontainer: {
    borderRadius: 56,
    backgroundColor: Colors.colorKhaki,
    padding: Padding.p_12,
    justifyContent: 'center',
  },
  text41: {
    color: Colors.colorBlack,
    fontSize: FontSize.size_24,
    fontFamily: FontFamily.pretendardVariable,
    fontWeight: '700',
  },
  text42: {
    fontSize: FontSize.size_18,
    lineHeight: 29,
    color: Colors.colorDimgray,
  },
  alramtextcontainer: {
    width: 182,
    gap: Gap.gap_2,
  },
  buttonscheckbox: {
    borderRadius: BorderRadius.br_6,
    borderColor: Colors.colorLightgray,
    borderWidth: 2,
    borderStyle: 'solid',
    overflow: 'hidden',
    backgroundColor: Colors.colorWhite,
  },
  buttonscheckboxWrapper: {
    justifyContent: 'flex-end',
    alignItems: 'center',
    height: 60,
    flexDirection: 'row',
  },
  routinecheck: {
    gap: 0,
    justifyContent: 'space-between',
  },
  cardsallroutines: {
    justifyContent: 'flex-end',
  },
});

export default ScheduleItem;
