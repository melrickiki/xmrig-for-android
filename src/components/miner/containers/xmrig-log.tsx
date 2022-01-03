import React from 'react';
import {View, StyleSheet, ViewProps} from 'react-native';
import { Badge, List } from 'react-native-paper';
import { IMinerLog } from '../../../core/session-data/session-data.interface';
import { MinerCard } from '../components/miner-card.component';

type LogViewProps = ViewProps & {
    data: IMinerLog[],
    disabled: boolean;
}

export const XMRigLogView:React.FC<LogViewProps> = ({
    data,
    disabled
}) => {
    return (
        <>
            <View>
                <MinerCard
                    title="stdout"
                    disabled={disabled}
                    style={{flex: 1}}
                    wrapInContent={false}
                >
                    {data.map((value, index) => (
                        <List.Item
                            key={`key-${index}`}
                            title={value.message.trim()}
                            style={styles.item}
                            titleStyle={{fontSize: 14}}
                            titleNumberOfLines={10}
                            right={props => <View {...props} style={styles.module}>
                                {value?.module && <Badge>{value?.module}</Badge>}
                            </View>}
                        />
                    ))}
                    
                </MinerCard>
            </View>
        </>
    )
};

const styles = StyleSheet.create({
    item: {
        paddingVertical: 0
    },
    module: {
        alignSelf: 'center'
    }
});