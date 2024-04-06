import { createUser, getUser } from '@/lib/userService';
import Link from 'next/link';

/**
 * Page component for user sign-up and retrieval.
 * 
 * @returns {JSX.Element} The Page component.
 */
export default function Page() {
    return (
        <div className="bg-red-500 h-screen border-2 border-black mt-2">
            {/* Sign-up form */}
            <h1 className="m-3">Enter a UserName and a Password to sign up!</h1>
            <form action={createUser}>
                <div className="m-3 block">
                    <label htmlFor="userName" className="mr-2">Username: </label>
                    <input
                        type="text"
                        id="userName"
                        name="userName"
                        className="border border-black rounded-md px-2 py-1"
                    />
                </div>
                <div className="m-3 block">
                    <label htmlFor="password" className="mr-2">Password: </label>
                    <input
                        type="text"
                        id="password"
                        name="password"
                        className="border border-black rounded-md px-2 py-1"
                    />
                </div>
                <div className="ml-5 mt-10">
                    <button className="bg-white rounded-lg py-2 px-4" type="submit">
                        <h1 className="text-red-700 font-bold text-lg">Sign up!</h1>
                    </button>
                </div>
            </form>

            {/* User retrieval form */}
            <form action={getUser}>
                <div className="m-3 block">
                    <label htmlFor="userName" className="mr-2">Username: </label>
                    <input
                        type="text"
                        id="userName"
                        name="userName"
                        className="border border-black rounded-md px-2 py-1"
                    />
                </div>
                <div className="m-3 block">
                    <label htmlFor="password" className="mr-2">Password: </label>
                    <input
                        type="text"
                        id="password"
                        name="password"
                        className="border border-black rounded-md px-2 py-1"
                    />
                </div>
                <div className="ml-5 mt-10">
                    <button className="bg-white rounded-lg py-2 px-4" type="submit">
                        <h1 className="text-red-700 font-bold text-lg">Get User!</h1>
                    </button>
                </div>

            </form>
        </div>
    );
}