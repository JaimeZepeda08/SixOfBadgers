'use client'
import React, { createContext, useState } from 'react';
import {reportType, userType} from "@/lib/Types";
import {useSession} from "next-auth/react";

const initTestData: userType = {
    userName: 'guest',
    email: '',
    image: '',
};

type userContextType = {
    userData: userType;
    setUserData: (value: userType) => void;
    savedGames: reportType[];
    setSavedGames: (value: reportType[]) => void;
}

export const UserContext = createContext<userContextType>({
    userData: initTestData,
    setUserData: () => {
    },
    savedGames: [],
    setSavedGames: () => {
    },
});

export const UserProvider = ({ children }: any) => {
    const [userData, setUserData] = useState<userType>(initTestData);
    const [savedGames, setSavedGames] =
        useState<reportType[]>([]);
    return (
        <UserContext.Provider value={{ userData, setUserData, savedGames, setSavedGames }}>
            {children}
        </UserContext.Provider>
    );
};
