import React from 'react';
import {
  requireNativeComponent,
  UIManager,
  Platform,
  ViewStyle,
  View,
  ActivityIndicator,
  Text,
} from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-big-image-viewer' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

type BigImageViewerProps = {
  url: string;
  style: ViewStyle;
  onSuccess: (event: { nativeEvent: { url: string } }) => void;
  onProgress: (event: {
    nativeEvent: { url: string; progress: number };
  }) => void;
};

const ComponentName = 'BigImageViewerView';

export const BigImageViewerView =
  UIManager.getViewManagerConfig(ComponentName) != null
    ? requireNativeComponent<BigImageViewerProps>(ComponentName)
    : () => {
        throw new Error(LINKING_ERROR);
      };

export const RNBigImageViewer = ({
  url,
  style,
}: {
  url: string;
  style?: ViewStyle;
}) => {
  const [loadedItems, setLoadedItems] = React.useState<
    Record<
      string,
      {
        progress: number;
        done?: 'true';
      }
    >
  >({});
  const isLoaded = loadedItems[url]?.done;
  return (
    <View style={[{ flex: 1 }, style]}>
      <BigImageViewerView
        url={url}
        style={{
          flex: isLoaded ? 1 : 0,
        }}
        onSuccess={(x) => {
          const url = x.nativeEvent.url;
          setLoadedItems(() => ({
            [url]: { progress: 100, done: 'true' },
          }));
        }}
        onProgress={(x) => {
          const url = x.nativeEvent.url;
          const progress = x.nativeEvent.progress;
          setLoadedItems(() => ({
            [url]: { progress: progress },
          }));
        }}
      />
      {!isLoaded && (
        <View
          style={{
            flex: 1,
            justifyContent: 'center',
            alignItems: 'center',
          }}
        >
          <ActivityIndicator
            style={{ width: 64, height: 64 }}
            color="#999999"
          />
          <Text style={{ color: '#999999' }}>
            {loadedItems?.[url]?.progress}%
          </Text>
        </View>
      )}
    </View>
  );
};
