import Link from "next/link";
import React from "react";

interface SimpleButtonProps {
  text: string;
  onClick?: () => void;
  className?: string;
}

interface SimpleButtonLinkProps extends SimpleButtonProps {
  href: string;
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

export const SimpleButtonLink: React.FC<SimpleButtonLinkProps> = ({
  text,
  onClick,
  href,
  className,
}) => {
  const containerClassName = `border-4 border-red-500 rounded-md shadow-md py-2 px-3 transition-colors duration-200 ease-in-out group hover:bg-red-500 hover:shadow-lg ${className}`;
  const textClassName = `text-red-500 font-bold group-hover:text-white`;

  return (
    <Link href={href}>
      <div className={containerClassName} onClick={onClick}>
        <h1 className={textClassName}>{text}</h1>
      </div>
    </Link>
  );
};
