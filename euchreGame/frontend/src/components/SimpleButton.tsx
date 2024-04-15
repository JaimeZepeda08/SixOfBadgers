import Link from "next/link";
import React from "react";

/**
 * Props for the SimpleButton component.
 */
interface SimpleButtonProps {
  text: string /** Text displayed on the button */;
  onClick?: () => void /** Function to be called when the button is clicked */;
  className?: string /** Custom CSS classes for styling */;
}

/**
 * Props for the SimpleButtonLink component, extends SimpleButtonProps with href and disabled props.
 */
interface SimpleButtonLinkProps extends SimpleButtonProps {
  href: string /** URL the button should link to */;
  disabled?: boolean /** Whether the button should be disabled */;
}

/**
 * Simple button component with red color theme.
 */
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

/**
 * Simple button component with green color theme.
 */
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

/**
 * Button component with red color theme and the ability to act as a link.
 * If disabled, renders a disabled button with gray color.
 */
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
