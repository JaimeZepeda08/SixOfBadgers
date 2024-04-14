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
    { name: "Rules", href: "/rules" },
    { name: "Stats", href: "/stats" },
  ];

  return (
    <nav className="bg-red-600 border border-black" style={{ zoom: 0.8 }}>
      <ul className="flex items-center justify-between">
        <li className="flex items-center">
          <Image
            src="/6ofBadgers_red.png"
            width={100}
            height={100}
            className="hidden md:block pt-4 pb-4 mr-8"
            alt="LOGO"
          />
          {links.map((link, index) => (
            <Link href={link.href} key={index}>
              <span
                className={clsx(
                  "block px-5 py-5 text-2xl rounded hover:bg-gray-200 hover:text-gray-800 hover:border-transparent cursor-pointer",
                  {
                    "text-white": pathname === link.href,
                  }
                )}
              >
                {link.name}
              </span>
            </Link>
          ))}
        </li>
        <li className="flex items-center mr-10">
          {/* add extra components to the nav bar */}
          {components?.map((component, index) => (
            <div key={index} className="mx-5">
              <div className="w-[40px] flex items-center justify-center">
                {component}
              </div>
            </div>
          ))}
        </li>
      </ul>
    </nav>
  );
};

export default TopNav;
