import { Modal, Image, View, Text, StyleSheet } from "react-native";
import { Theme } from "../../styles/theme";
import { useAuthStore } from "../../store/authStore";

const authStore = useAuthStore;


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
    const { user } = authStore.getState();
    const isGuardian = user.role === 'GUARDIAN'; // true: 보호자 | false: 피보호자

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
            <View>
                {isImageVisible && <Image source={require('@assets/images/InviteSuccess.png')} />}
                <Text>
                    {name && <Text >{name}</Text>}
                    {title && <Text>{title}</Text>}
                </Text>
                    {description && <Text>{description}</Text>}
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
})