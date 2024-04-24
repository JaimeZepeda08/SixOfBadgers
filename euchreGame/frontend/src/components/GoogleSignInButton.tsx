'use client';
import {useSession, signIn, signOut, getSession} from 'next-auth/react';
import {useContext, useEffect, useState} from "react";
import {UserContext} from "@/lib/UserContext";
import {userHandler} from "@/lib/UserService";

/**
 * Component that displays a button to sign in with Google OAuth when user is not authenticated.
 * If authenticated, shows user image and name with a sign out button to change accounts.
 * @constructor
 */
export function signInGoogle() {
    signIn('google').then(() => {
        getSession();
    })
}

export default function GoogleSignInButton() {
    const [ loggedIn, setLoggedIn ] = useState(false);
    const {
        userData,
        setUserData,
        savedGames,
        setSavedGames,
    } = useContext(UserContext);

    /**
     * Function to sign in with Google OAuth and retrieve user session to be used in client
     */
    // Get the current user authenticated session from google sign in
    const { data: data, status } = useSession();

    // Handles google sign in login data: calls userHandler to save User to db and sets userData for userContext
    useEffect(() => {
        async function login() {
            if(userData.userName === "guest" && data?.user) {
                setUserData({
                    userName: data.user.name,
                    email: data.user.email,
                    image: data.user.image,
                });
            }

            if(!loggedIn && userData.userName !== "guest") {
                await userHandler(userData.email, userData.userName, "save");
                setLoggedIn(true);
            }
        }
        login();
    },[loggedIn, setLoggedIn, data?.user, setUserData, userData]);

    if (status === 'loading') return <h1> loading... please wait</h1>;
    if (status === 'authenticated') {
        return (
            <div className={'px-7 sm:px-0 max-w-sm'}>
                <div onClick={async () => signOut()}
                     className={'text-black w-full bg-600 hover:bg-red-800 focus:ring-4 ' +
                         'focus:outline-none focus:ring-[#4285F4]/50 font-medium rounded-lg text-xl px-5 py-2.5 text-center ' +
                         'inline-flex items-center justify-between mr-2 mb-2'}>
                    <img className={'mr-2 -ml-2 w-14 h-14'} src={userData.image || ""} alt={userData.userName + ' photo'}/>
                    Sign Out
                </div>
            </div>
        );
    }
    return (
       <div className={'px-7 sm:px-0 max-w-sm'}>
           <div onClick={signInGoogle}
                className={'text-black w-full bg-600 hover:bg-red-800 focus:ring-4 ' +
                    'focus:outline-none focus:ring-[#4285F4]/50 font-medium rounded-lg text-xl px-5 py-2.5 text-center ' +
                    'inline-flex items-center justify-between mr-2 mb-2'}>
               <svg className="mr-2 -ml-1 w-6 h-6" aria-hidden="true" focusable="false" data-prefix="fab"
                    data-icon="google" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 488 512">
                   <path fill="currentColor"
                         d="M488 261.8C488 403.3 391.1 504 248 504 110.8 504 0 393.2 0 256S110.8 8 248 8c66.8 0 123
                         24.5 166.3 64.9l-67.5 64.9C258.5 52.6 94.3 116.6 94.3 256c0 86.5 69.1 156.6 153.7 156.6 98.2 0
                         135-70.4 140.8-106.9H248v-85.3h236.1c2.3 12.7 3.9 24.9 3.9 41.4z"></path>
               </svg>
               Sign In
           </div>
       </div>
    );
}