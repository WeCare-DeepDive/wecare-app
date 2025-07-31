// components/modal/RoutineModal.jsx
import { Modal, Image, View, Text, StyleSheet, TouchableOpacity } from "react-native";
import { Theme } from "../../styles/theme";
import useUserInfo from "../../hooks/useUserInfo";
import CustomButton from "../buttons/Button";

const RoutineModal = ({
    isImageVisible = false,
    title,
    name,
    description,
    cancelButtonText,
    confirmButtonText,
    onCancel,
    onConfirm,
    isVisible,
    setIsVisible,
    onClose,
    ...props
}) => {

    // 보호자 피보호자 확인
    const { user, isDependent } = useUserInfo({useMock: true});
    const isGuardian = !isDependent; // true: 보호자 | false: 피보호자

    // 보호자,피보호자 글자 크기
    const titleFontSize = isGuardian ? Theme.FontSize.size_22 : Theme.FontSize.size_24;
    const descriptionFontSize = isGuardian ? Theme.FontSize.size_20 : Theme.FontSize.size_22;
    
    return (
        <Modal
            visible={isVisible}
            onRequestClose={onClose}
            transparent={true}
            animationType="fade"
            style={styles.modal}
        >
            <View style={[styles.view, {position: 'absolute', bottom: 0, left: 0, right: 0, zIndex: 1000}]}>
                {isImageVisible && <Image source={require('@assets/images/InviteSuccess.png')} style={styles.wecare1Icon} resizeMode='cover' />}
                <View style={styles.routinepopup}>
                    <Text style={[styles.text, styles.textFlexBox, {fontSize: titleFontSize}]}>
                        {name && <Text style={styles.text1}>{name}</Text>}
                        {title && <Text style={styles.text2}>{title}</Text>}
                    </Text>
                    <Text style={[styles.parent, {fontSize: descriptionFontSize}]}>
                        {description && <Text style={styles.text3}>{description}</Text>}
                        {isGuardian && <Text style={[styles.text4, styles.textFlexBox]}>{'(식사, 크게 웃기, 산책 하기 등)'}</Text>}
                    </Text>
                </View>
                <View style={[styles.buttonContainer, {width: '100%', gap: 10, display: 'flex', flexDirection: 'row', alignItems: 'center'}]}>
                    <View style={[styles.btnctaParent, styles.buttonshalfFlexBox]}>
                        <TouchableOpacity
                            style={[styles.btnCancel, styles.btnctaFlexBox]}
                            onPress={onCancel}
                            activeOpacity={0.8}
                        >
                            <Text style={[styles.text5, styles.textTypo]}>{cancelButtonText}</Text>
                        </TouchableOpacity>
                        <TouchableOpacity
                            style={[styles.btnConfirm, styles.btnctaFlexBox]}
                            onPress={onConfirm}
                            activeOpacity={0.8}
                        >
                            <Text style={[styles.text6, styles.textTypo]}>{confirmButtonText}</Text>
                        </TouchableOpacity>
                    </View>
                </View>
            </View>
        </Modal>
    )
}

export default RoutineModal;

const styles = StyleSheet.create({
    modal: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: Theme.Colors.colorWhite,
        
    },
    view: {
        width: "100%",
        shadowColor: Theme.Colors.colorGray,
        shadowOffset: {
              width: 0,
              height: 1
        },
        shadowRadius: 10,
        elevation: 10,
        shadowOpacity: 1,
        
        borderTopLeftRadius: 20,
        borderTopRightRadius: 20,

        overflow: "hidden",
        paddingHorizontal: Theme.Padding.p_20,
        paddingTop: Theme.Padding.p_30,
        paddingBottom: Theme.Padding.p_20,
        gap: 20,
        alignItems: "center",
        backgroundColor: Theme.Colors.colorWhite,
        flex: 1 ,
    },  
    textFlexBox: {
        textAlign: "center",
        fontFamily: Theme.FontFamily.pretendard,
        alignSelf: "stretch"
  },
  buttonshalfFlexBox: {
        flexDirection: "row",
        alignItems: "center"
  },
  btnctaFlexBox: {
        paddingVertical: Theme.Padding.p_12,
        paddingHorizontal: Theme.Padding.p_30,    
        justifyContent: "center",
        borderRadius: Theme.BorderRadius.br_10,
        flexDirection: "row",
        alignItems: "center",
        flex: 1
  },
  textTypo: {
        fontFamily: Theme.FontFamily.nanumR,
        fontWeight: "700",
        lineHeight: 32,
        fontSize: Theme.FontSize.size_20,
        textAlign: "center"
  },
  wecare1Icon: {
        width: 298,
        height: 197
  },
  text1: {
        color: Theme.Colors.colorMediumslateblue100
  },
  text2: {
        color: Theme.Colors.colorBlack
  },
  text: {
        fontSize: 22,
        lineHeight: 33,
        fontWeight: "600"
  },
  text3: {
        lineHeight: 30,
        fontWeight: "500",
        // fontSize: Theme.FontSize.size_20,
        color: Theme.Colors.colorBlack,
        textAlign: "center",
        fontFamily: Theme.FontFamily.pretendard,
        alignSelf: "stretch"
  },
  text4: {
        fontSize: 18,
        lineHeight: 29,
        color: Theme.Colors.colorDimgray
  },
  parent: {
      alignSelf: "stretch",
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'center',
      flex: 1,
  },
  routinepopup: {
        width: 353,
        gap: 8,
        alignItems: "center",
        paddingHorizontal: 10,
  },  
  text5: {
        color: Theme.Colors.colorMediumslateblue200
  },
  btnCancel: {
        borderStyle: "solid",
        borderColor: Theme.Colors.colorMediumslateblue100,
        borderWidth: 1,
        backgroundColor: Theme.Colors.colorWhite
  },
  text6: {
        color: Theme.Colors.colorWhite
  },
  btnConfirm: {
        backgroundColor: Theme.Colors.colorMediumslateblue200
  },
  btnctaParent: {
        gap: 10,
        flex: 1
  },
  buttonshalf: {
        alignSelf: "stretch"
  },
})