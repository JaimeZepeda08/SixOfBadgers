import {getHello} from "@/lib/userService";

export default function Home() {
    const hello = getHello();
    return (
        <div className={'flex flex-col items-center h-full'}>
            <div className={'flex justify-center w-full pt-4'}>
                {hello}
            </div>
        </div>
    );
}
