"use client";

import Link from "next/link";
import { IoPersonCircleOutline } from "react-icons/io5";

const Profile = ({}) => {
  return (
    <Link href="/signup" className="w-full h-full">
      <IoPersonCircleOutline className="h-full w-full hover:text-white" />
    </Link>
  );
};

export default Profile;
