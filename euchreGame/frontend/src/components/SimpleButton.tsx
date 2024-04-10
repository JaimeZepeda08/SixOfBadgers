import Link from "next/link";
import React from "react";

interface SimpleButtonProps {
  text: string;
  onClick?: () => void;
  className?: string;
}

interface SimpleButtonLinkProps extends SimpleButtonProps {
  href: string;
  disabled?: boolean;
}

export const SimpleButtonRed: React.FC<SimpleButtonProps> = ({
  text,
  onClick,
  className,
}) => {
  const containerClassName = `border-4 border-red-500 rounded-md shadow-md py-2 px-3 transition-colors duration-200 ease-in-out group hover:bg-red-500 hover:shadow-lg ${className}`;
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
  className,
}) => {
  const containerClassName = `border-4 border-green-500 rounded-md shadow-md py-2 px-3 transition-colors duration-200 ease-in-out group hover:bg-green-500 hover:shadow-lg ${className}`;
  const textClassName = `text-green-500 font-bold group-hover:text-white`;

  return (
    <div className={containerClassName} onClick={onClick}>
      <h1 className={textClassName}>{text}</h1>
    </div>
  );
};

export const SimpleButtonLinkRed: React.FC<SimpleButtonLinkProps> = ({
  text,
  onClick,
  className,
  href,
  disabled,
}) => {
  const containerClassName = `border-4 border-red-500 rounded-md shadow-md py-2 px-3 transition-colors duration-200 ease-in-out group hover:bg-red-500 hover:shadow-lg ${className}`;
  const textClassName = `text-red-500 font-bold group-hover:text-white`;

  if (disabled) {
    return (
      <div
        className={`${containerClassName} border-gray-500 hover:bg-gray-500`}
        onClick={onClick}
      >
        <h1 className={`${textClassName} text-gray-500`}>{text}</h1>
      </div>
    );
  }

  return (
    <Link href={href}>
      <div className={containerClassName} onClick={onClick}>
        <h1 className={textClassName}>{text}</h1>
      </div>
    </Link>
  );
};
