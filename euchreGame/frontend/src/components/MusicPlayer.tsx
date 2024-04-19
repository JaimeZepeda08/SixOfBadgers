"use client";

import React, { useState, useEffect } from "react";
import { IoMdVolumeHigh } from "react-icons/io";
import { IoVolumeMuteSharp } from "react-icons/io5";

/**
 * The props expected by the MusicPlayer component.
 */
interface MusicPlayerProps {
  src: string;
}

/**
 * A simple React component for playing audio.
 */
const MusicPlayer = ({ src }: MusicPlayerProps) => {
  // State to manage whether the audio is currently playing or not
  const [isPlaying, setIsPlaying] = useState(false);
  // Reference to the audio element
  const audioRef = React.createRef<HTMLAudioElement>();

  // useEffect hook to handle starting and pausing audio playback
  useEffect(() => {
    const audio = audioRef.current;
    if (audio) {
      if (isPlaying) {
        audio.play(); // If isPlaying is true, play the audio
      } else {
        audio.pause(); // If isPlaying is false, pause the audio
      }
    }
  }, [isPlaying, audioRef]); // Run this effect whenever isPlaying changes

  // Function to toggle the playing state
  const togglePlay = () => {
    setIsPlaying(!isPlaying);
  };

  return (
    <div
      className="w-full h-full cursor-pointer hover:text-white mt-1"
      style={{ zoom: 2 }}
    >
      {/* Audio element with source */}
      <audio ref={audioRef}>
        <source src={src} />
        This browser does not support audio.
      </audio>
      {/* Button to toggle play/pause */}
      {isPlaying ? (
        <button className="w-full h-full" onClick={togglePlay}>
          <IoMdVolumeHigh />
        </button>
      ) : (
        <button className="w-full h-full" onClick={togglePlay}>
          <IoVolumeMuteSharp />
        </button>
      )}
    </div>
  );
};

export default MusicPlayer;
