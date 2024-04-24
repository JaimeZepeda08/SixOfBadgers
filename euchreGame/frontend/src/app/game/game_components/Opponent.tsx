import React from 'react';
import Hand from '@/components/Hand';

/**
 * Opponent component.
 * 
 * @param {object} props - Component props.
 * @param {string} props.name - Name of the opponent.
 * @param {object} props.player - Opponent's player data.
 * @param {array} props.cards - Cards held by the opponent.
 * @param {function} props.onCardSelect - Function to handle card selection.
 * @param {object} props.tooltipVisible - State indicating if tooltip is visible.
 * @param {function} props.toggleTooltip - Function to toggle tooltip visibility.
 * @param {string} props.padding - Padding style for the opponent's info.
 * @returns {JSX.Element} Opponent component.
 */
const Opponent = ({ name, player, cards, onCardSelect, tooltipVisible, toggleTooltip, padding }: any) => {
  return (
    <div
      className={`absolute ${player.position}`}
      style={{ transform: `rotate(${player.rotation}deg)` }}
    >
    {/* Render opponent's hand */}
      <Hand cards={cards} onCardSelect={onCardSelect} />
      <div
        className={`text-center ${padding}`}
        style={{ marginTop: '10px', transform: `rotate(${-player.rotation}deg)`}}
      >
        <div
          onMouseEnter={() => toggleTooltip(name)}
          onMouseLeave={() => toggleTooltip(name)}
          className="inline-block bg-white rounded-full px-4 py-1 text-sm font-semibold text-gray-700 relative cursor-pointer"
        >
          {name}
          {/* Render tooltip if visible */}
          {tooltipVisible[player.key] && (
            <div className="absolute z-10 w-40 p-2 -mt-30 left-1/2 transform -translate-x-1/2 text-white bg-gray-900 rounded-lg shadow-md">
              Wins: {player.wins}
              <br />
              Games Played: {player.gamesPlayed}
            </div>
          )}
        </div>
        <h2 className="text-white mt-2">Score: {player.score}</h2>
      </div>
    </div>
  );
}

export default Opponent;
