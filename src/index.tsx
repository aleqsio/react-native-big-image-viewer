import {
  requireNativeComponent,
  UIManager,
  Platform,
  ViewStyle,
} from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-big-image-viewer' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

type BigImageViewerProps = {
  color: string;
  style: ViewStyle;
};

const ComponentName = 'BigImageViewerView';

export const BigImageViewerView =
  UIManager.getViewManagerConfig(ComponentName) != null
    ? requireNativeComponent<BigImageViewerProps>(ComponentName)
    : () => {
        throw new Error(LINKING_ERROR);
      };
