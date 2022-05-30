/* eslint-disable react-native/no-inline-styles */
import * as React from 'react';
import {
    StyleSheet,
    View,
    Text,
    Button,
    Pressable,
    TouchableOpacity,
} from 'react-native';
import StepIndicator from 'react-native-step-indicator';
import {Icon} from 'react-native-elements';
import StatusOrdersEnum from '~/constants/StatusOrdersEnum';
import {MediumText, SmallText} from '~/components/Text';
import {useEffect} from "react";
import orderApi from "~/api/orderApi";
import moment from 'moment';
import TypeUser from '~/constants/TypeUser';

const secondIndicatorStyles = {
    stepIndicatorSize: 30,
    currentStepIndicatorSize: 40,
    separatorStrokeWidth: 2,
    currentStepStrokeWidth: 3,
    stepStrokeCurrentColor: '#fe7013',
    stepStrokeWidth: 3,
    separatorStrokeFinishedWidth: 4,
    stepStrokeFinishedColor: '#fe7013',
    stepStrokeUnFinishedColor: '#aaaaaa',
    separatorFinishedColor: '#fe7013',
    separatorUnFinishedColor: '#aaaaaa',
    stepIndicatorFinishedColor: '#fe7013',
    stepIndicatorUnFinishedColor: '#ffffff',
    stepIndicatorCurrentColor: '#ffffff',
    stepIndicatorLabelFontSize: 13,
    currentStepIndicatorLabelFontSize: 13,
    stepIndicatorLabelCurrentColor: '#fe7013',
    stepIndicatorLabelFinishedColor: '#ffffff',
    stepIndicatorLabelUnFinishedColor: '#aaaaaa',
    labelColor: '#999999',
    labelSize: 13,
    currentStepLabelColor: '#fe7013',
};
const labels = ['Order Received', 'Assign to Driver', 'Pickup package', 'Delivered', 'Done'];

export function OrderStatus({orderId, currentStep, typeUser}) {
    const [currentPosition, setCurrentPosition] = React.useState(0);
    const [positionCancel, setPositionCancel] = React.useState(-1);

    useEffect(() => {
        if (currentStep) {
            setCurrentPosition(labels.indexOf(currentPosition.step));
            if (currentStep.status === 'CANCEL') {
                setPositionCancel(labels.indexOf(currentPosition.step));
            }
        }

    }, [orderId])

    const handleButtonFinish = () => {
        if (currentPosition < 3) {
            setCurrentPosition(currentPosition + 1);
            const status = {
              step: labels[currentPosition + 1],
              status: 'FINISHED',
              timestamp: moment().valueOf()
            }
            updateStatusOrder(status);
        }
    };
    const handleCancelButton = () => {
        setPositionCancel(currentPosition);
        const status = {
            step: labels[currentPosition + 1],
            status: 'CANCEL',
            timestamp: moment().valueOf()
        }
        updateStatusOrder(status);
    };

    const updateStatusOrder = (status)=>{
      orderApi.updateOrderStatus(orderId, status)
          .then(res => console.log("update order status successful res: ", res.Data))
          .catch(res => console.log("update order status faile res: ", res))
    }

    const getStepIndicatorIconConfig = ({position, stepStatus}) => {
        const iconConfig = {
            name: 'checkmark-sharp',
            type: 'ionicon',
            color: '#517fa4',
        };
        if (position === positionCancel) {
            iconConfig.color = 'red';
            iconConfig.name = 'close-sharp';
        }
        return iconConfig;
    };

    const renderStepIndicator = params => (
        <Icon {...getStepIndicatorIconConfig(params)}></Icon>
    );

    const renderLabel = ({position, label, currentPosition}) => {
        return (
            <Text
                style={
                    position === currentPosition
                        ? styles.stepLabelSelected
                        : styles.stepLabel
                }>
                {label}
            </Text>
        );
    };

    return (
        <View style={styles.container}>
            <View style={styles.title}>
                <SmallText text="Order Status"/>
            </View>
            <View style={styles.stepIndicator}>
                <StepIndicator
                    customStyles={secondIndicatorStyles}
                    currentPosition={currentPosition}
                    // onPress={onStepPress}
                    renderStepIndicator={renderStepIndicator}
                    labels={labels}
                    stepCount={4}
                    renderLabel={renderLabel}
                />
            </View>
            {typeUser === TypeUser.DRIVER ? (
                <View style={styles.containerButton}>
                    <TouchableOpacity
                        style={styles.btnCancel}
                        onPress={handleCancelButton}
                        disabled={currentPosition >= 3 || positionCancel != -1}>
                        <MediumText text="Cancel" props={{alignContent: 'center'}}/>
                    </TouchableOpacity>
                    <TouchableOpacity
                        style={styles.btnFinish}
                        onPress={handleButtonFinish}
                        disabled={positionCancel != -1 || currentPosition >= 3}>
                        <MediumText
                            text={
                                'Finish ' +
                                (currentPosition < 3 && currentPosition > 0
                                    ? labels[currentPosition]
                                    : '')
                            }
                        />
                    </TouchableOpacity>
                </View>
            ) : (
                <View style={styles.containerButton}>
                    <TouchableOpacity
                        style={styles.btnDone}
                        onPress={handleButtonFinish}
                        disabled={positionCancel != -1 || currentPosition >= 3}>
                        <MediumText
                            text={
                                'Done'
                            }
                        />
                    </TouchableOpacity>
                </View>
            )}
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#ffffff',
        paddingHorizontal: 5,
    },
    title: {
        paddingBottom: 5,
    },
    stepIndicator: {
        paddingVertical: 10,
    },
    stepLabel: {
        fontSize: 12,
        textAlign: 'center',
        fontWeight: '500',
        color: '#999999',
    },
    stepLabelSelected: {
        fontSize: 12,
        textAlign: 'center',
        fontWeight: '500',
        color: '#4aae4f',
    },
    containerButton: {
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'center',
        alignContent: 'center',
        alignItems: 'center',
    },
    btnCancel: {
        backgroundColor: '#EE5448',
        width: '30%',
        elevation: 1,
        borderRadius: 5,
        justifyContent: 'center',
        alignItems: 'center',
    },
    btnFinish: {
        backgroundColor: '#4aae4f',
        width: '70%',
        elevation: 1,
        borderRadius: 5,
        marginLeft: 5,
        justifyContent: 'center',
        alignItems: 'center',
    },
    btnDone: {
        backgroundColor: '#4aae4f',
        width: '70%',
        elevation: 1,
        borderRadius: 5,
        justifyContent: 'center',
        alignItems: 'center',
    },
});
