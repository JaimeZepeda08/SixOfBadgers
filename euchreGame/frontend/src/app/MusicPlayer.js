// MusicPlayer.js
import React, { useState, useEffect } from 'react';

const MusicPlayer = ({ src }) => {
  const [audio] = useState(new Audio(src));

  useEffect(() => {
    audio.loop = true; // Set the music to loop
    audio.play();

    return () => {
      audio.pause();
      audio.currentTime = 0;
    };
  }, [audio]);

  return null; // Music player doesn't need to render anything visible
};

export default MusicPlayer;
