'use client';
import React, { use, useState } from 'react';

/**
 * Page component for configuring game settings. User is able to set specific
 * settings for the game and when they are ready they press the start game button
 * and they will be sent to the play page.
 * 
 * @returns {JSX.Element} The page component.
 */
export default function Page() {
  // State variables for game settings
  const [numberOfPlayers, setNumberOfPlayers] = useState(1);
  const [timePerTurn, setTimePerTurn] = useState('');
  const [pointsToWin, setPointsToWin] = useState('');
  const [numberOfRounds, setNumberOfRounds] = useState('');

  const handlePlayerChange = (event: any) => {
    setNumberOfPlayers(parseInt(event.target.value));
  };

  const handleTimePerTurnChange = (event: any) => {
    setTimePerTurn(event.target.value);
  };

  const handlePointsToWinChange = (event: any) => {
    setPointsToWin(event.target.value);
  };

  const handleNumberOfRoundsChange = (event: any) => {
    setNumberOfRounds(event.target.value);
  };

  return (
    <div className="bg-red-500 h-screen border-2 border-black mt-2">
      {/* configuration for how many real players there will be for the game */}
      <h1 className="m-3">Number of Players</h1>
      <div className="mb-2 block">
        <input
          className="appearance-none border border-black rounded-full w-5 h-5 checked:bg-black checked:border-transparent ml-8"
          type="radio"
          name="flexRadioDefault"
          id="radioDefault01"
          value="1"
          checked={numberOfPlayers === 1}
          onChange={handlePlayerChange}
        />
        <label
          className="ml-2 cursor-pointer"
          htmlFor="radioDefault01"
        >
          1 Player
        </label>
      </div>
      <div className="mb-2 block">
        <input
          className="appearance-none border border-black rounded-full w-5 h-5 checked:bg-black checked:border-transparent ml-8"
          type="radio"
          name="flexRadioDefault"
          id="radioDefault02"
          value="2"
          checked={numberOfPlayers === 2}
          onChange={handlePlayerChange}
        />
        <label
          className="ml-2 cursor-pointer"
          htmlFor="radioDefault02"
        >
          2 Players
        </label>
      </div>
      <div className="mb-2 block">
        <input
          className="appearance-none border border-black rounded-full w-5 h-5 checked:bg-black checked:border-transparent ml-8"
          type="radio"
          name="flexRadioDefault"
          id="radioDefault03"
          value="3"
          checked={numberOfPlayers === 3}
          onChange={handlePlayerChange}
        />
        <label
          className="ml-2 cursor-pointer"
          htmlFor="radioDefault03"
        >
          3 Players
        </label>
      </div>
      <div className="mb-2 block">
        <input
          className="appearance-none border border-black rounded-full w-5 h-5 checked:bg-black checked:border-transparent ml-8"
          type="radio"
          name="flexRadioDefault"
          id="radioDefault04"
          value="4"
          checked={numberOfPlayers === 4}
          onChange={handlePlayerChange}
        />
        <label
          className="ml-2 cursor-pointer"
          htmlFor="radioDefault04"
        >
          4 Players
        </label>
      </div>

      {/* setting for number of points a player needs to win */}
      <div className="m-3 block">
        <label htmlFor="timePerTurn1" className="mr-2">Time Per Turn(Seconds):</label>
        <input
          type="text"
          id="timePerTurn1"
          value={timePerTurn}
          onChange={handleTimePerTurnChange}
          className="border border-black rounded-md px-2 py-1"
        />
      </div>

      {/* setting for number of points a player needs to win */}
      <div className="m-3 block">
        <label htmlFor="pointsToWin" className="mr-2">Points To Win:</label>
        <input
          type="text"
          id="timePerTurn2"
          value={pointsToWin}
          onChange={handlePointsToWinChange}
          className="border border-black rounded-md px-2 py-1"
        />
      </div>

      {/* setting for number of rounds the game will go for */}
      <div className="m-3 block">
        <label htmlFor="roundsToWin" className="mr-2">Number Of Rounds:</label>
        <input
          type="text"
          id="timePerTurn3"
          value={numberOfRounds}
          onChange={handleNumberOfRoundsChange}
          className="border border-black rounded-md px-2 py-1"
        />
      </div>
      
      {/* button that will save the set settings for the game and send the user to the game */}
      <div className="ml-5 mt-10">
        <button className="bg-white rounded-lg py-2 px-4">
            <h1 className="text-red-700 font-bold text-lg">Start Game!</h1>
        </button>
      </div>
    </div>
  );
}
