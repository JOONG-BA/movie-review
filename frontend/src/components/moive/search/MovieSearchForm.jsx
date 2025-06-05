import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { IoMdSearch } from "react-icons/io";

export default function MovieSearchForm() {
    const [query, setQuery] = useState("");
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        if (query.trim()) {
            navigate(`/movie/search?query=${encodeURIComponent(query)}`);
        }
    };


    return (
        <form className="flex relative" onSubmit={handleSubmit}>
            <Input
                className="bg-gray-800 rounded-xs w-[250px] border-0"
                placeholder="찾는 영화가 있으신가요?"
                value={query}
                onChange={(e) => setQuery(e.target.value)}
            />
            <Button
                type="submit"
                className="absolute bg-0 right-0 text-2xl p-0 hover:bg-0 text-gray-400 hover:text-white cursor-pointer"
            >
                <IoMdSearch className="size-6" />
            </Button>
        </form>
    );
}
