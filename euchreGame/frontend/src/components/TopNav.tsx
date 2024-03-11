"use client";

import Link from "next/link";
import Image from "next/image";
import clsx from "clsx";
import { usePathname } from "next/navigation";

// keep list of all pages for the navbar to go through in order
export default function TopNav() {
  const pathname = usePathname();

  const links = [
    { name: "Home", href: "/pages/home" },
    { name: "Rules", href: "/pages/rules" },
    { name: "Play", href: "/pages/play" },
    { name: "About", href: "/pages/about" },
    { name: "Stats", href: "/pages/stats" },
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
          <li key={index} className="mr-4">
            <Link href={link.href}>
              <span
                className={clsx(
                  "block px-5 py-5 mt-7 text-2xl rounded hover:bg-gray-200 hover:text-gray-800 hover:border-transparent text-decoration-line: underline cursor-pointer",
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
      </ul>
    </nav>
  );
}
