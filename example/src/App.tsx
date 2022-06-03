import * as React from 'react';

import { Button } from 'react-native';
import { RNBigImageViewer } from 'react-native-big-image-viewer';

export default function App() {
  const [counter, setCounter] = React.useState(3220);
  return (
    <>
      <Button
        onPress={() => setCounter((c) => c + 1)}
        title={'Now showing - ' + counter}
      />
      <Button onPress={() => setCounter((c) => c - 1)} title={'Prev'} />
      <RNBigImageViewer
        url={`https://photojournal.jpl.nasa.gov/jpeg/PIA0${counter}.jpg`}
        style={{ backgroundColor: 'black' }}
      />
    </>
  );
}
