import Modal from 'react-modal';
import Link from 'next/link';
import Hand from '@/components/Hand';

/**
 * GameModal component.
 * 
 * @param {object} props - Component props.
 * @param {boolean} props.isOpen - Flag indicating if the modal is open.
 * @returns {JSX.Element} GameModal component.
 */
const GameModal = ({ isOpen }) => {

    const opponents = [
        { suit: "?", value: "?" },
        { suit: "?", value: "?" },
        { suit: "?", value: "?" },
        { suit: "?", value: "?" },
        { suit: "?", value: "?" },
    ];    

  return (
    <Modal
      isOpen={isOpen}
      contentLabel="Example Modal"
      style={{
        content: {
          width: '66%',
          height: '75%',
          margin: 'auto',
          borderRadius: '20px',
          background: 'linear-gradient(to right, red, pink)', // Red-pink gradient background
        },
        overlay: {
          backgroundColor: 'rgba(0, 0, 0, 0.5)', // Semi-transparent overlay
        },
      }}
    >
      <div className="flex justify-center items-center h-full">
        <h2 className="text-6xl mt-20">Team 1 Won The Game!</h2>
        <div className={`absolute mb-20`}>
            <Hand cards={opponents} onCardSelect={null} />
        </div>
      </div>

      {/* Link to return home */}
      <Link href="/home" className="absolute bottom-4 right-4 font-bold py-2 px-4 border border-gray-300">
        Return Home
      </Link>
    </Modal>
  );
};

export default GameModal;
