"use client";

import Link from "next/link";
import Image from "next/image";
import clsx from "clsx";
import { usePathname } from "next/navigation";

interface TopNavProps {
  components?: React.ReactNode[];
}

/**
 * TopNav component for displaying a horizontal navbar with navigation links.
 * 
 * @param {object} props The props object.
 * @param {React.ReactNode[]} [props.components] Additional components to be added to the navbar.
 * @returns {JSX.Element} The TopNav component.
 */
const TopNav = ({ components }: TopNavProps) => {
  const pathname = usePathname();

  const links = [
    { name: "Home", href: "/home" },
    { name: "Play", href: "/play" },
    { name: "Rules", href: "/rules" },
    { name: "Stats", href: "/stats" },
  ];

  return (
    // create horizontal navbar
    <nav className="bg-red-500  border border-black">
      <ul className="flex">
        <Image
          src="/6ofBadgers_red.png"
          width={100}
          height={100}
          className="hidden md:block pt-4 pb-4 mr-8"
          alt="LOGO"
        />
        {/* set of all links that will be highlighted when hovered over
            navlink will stay white when we are on that page */}
        {links.map((link, index) => (
          <li key={index} className="mx-4">
            <Link href={link.href}>
              <span
                className={clsx(
                  "block px-5 py-5 mt-7 text-2xl rounded hover:bg-gray-200 hover:text-gray-800 hover:border-transparent cursor-pointer",
                  {
                    "text-white": pathname === link.href,
                  }
                )}
              >
                {link.name}
              </span>
            </Link>
          </li>
        ))}
        {/* add extra components to the nav bar */}
        {components?.map((component, index) => (
          <li key={index} className="mx-8">
            <div className=" h-full w-[40px] flex items-center justify-center">
              {component}
            </div>
          </li>
        ))}
      </ul>
    </nav>
  );
};

export default TopNav;
