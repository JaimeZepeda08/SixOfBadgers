import React from "react";

interface SimpleButtonProps {
  text: string;
  onClick: () => void;
}

export const SimpleButtonRed: React.FC<SimpleButtonProps> = ({
  text,
  onClick,
}) => {
  const containerClassName = `border-4 border-red-500 rounded-md shadow-md py-2 px-3 transition-colors duration-200 ease-in-out group hover:bg-red-500 hover:shadow-lg`;
  const textClassName = `text-red-500 font-bold group-hover:text-white`;

  return (
    <div className={containerClassName} onClick={onClick}>
      <h1 className={textClassName}>{text}</h1>
    </div>
  );
};

export const SimpleButtonGreen: React.FC<SimpleButtonProps> = ({
  text,
  onClick,
}) => {
  const containerClassName = `border-4 border-green-500 rounded-md shadow-md py-2 px-3 transition-colors duration-200 ease-in-out group hover:bg-green-500 hover:shadow-lg`;
  const textClassName = `text-green-500 font-bold group-hover:text-white`;

  return (
    <div className={containerClassName} onClick={onClick}>
      <h1 className={textClassName}>{text}</h1>
    </div>
  );
};
