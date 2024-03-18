"use client";

import React, { useState, useEffect } from "react";
import { IoMdVolumeHigh } from "react-icons/io";
import { IoVolumeMuteSharp } from "react-icons/io5";

interface MusicPlayerProps {
  src: string;
}

const MusicPlayer = ({ src }: MusicPlayerProps) => {
  const [audio] = useState(new Audio(src));
  const [isMuted, setIsMuted] = useState(false);

  useEffect(() => {
    audio.loop = true;
    audio.play();

    return () => {
      audio.pause();
      audio.currentTime = 0;
    };
  }, [audio]);

  const toggleSound = () => {
    if (audio.volume === 0) {
      audio.volume = 1;
      setIsMuted(false);
    } else {
      audio.volume = 0;
      setIsMuted(true);
    }
  };

  return (
    <div
      onClick={toggleSound}
      className="w-full h-full cursor-pointer hover:text-white"
    >
      {isMuted ? (
        <IoVolumeMuteSharp className="w-full h-full" />
      ) : (
        <IoMdVolumeHigh className="w-full h-full" />
      )}
    </div>
  );
};

export default MusicPlayer;
