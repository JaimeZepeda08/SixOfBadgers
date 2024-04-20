import Modal from 'react-modal';
import Link from 'next/link';

const GameModal = ({ isOpen }) => {
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
        <h2 className="text-6xl">Team 1 Won The Game!</h2>
      </div>
      <Link href="/home" className="absolute bottom-4 right-4 font-bold py-2 px-4 border border-gray-300">
        Return Home
      </Link>
    </Modal>
  );
};

export default GameModal;
