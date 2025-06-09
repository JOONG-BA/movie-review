import Slider from "react-slick";
import { FaUser } from "react-icons/fa";
import {NextArrow} from "@/components/slider/PrevArrow.jsx";
import {PrevArrow} from "@/components/slider/NextArrow.jsx";

const chunkGroup = (arr, size) => {
    const result = [];
    for (let i = 0; i < arr.length; i += size) {
        result.push(arr.slice(i, i + size));
    }
    return result;
};

export default function CastAndCrew({ casts = [], directors = [] }) {
    const combined = [
        ...casts.map((p) => ({ ...p, type: "cast" })),
        ...directors.map((p) => ({ ...p, type: "director" }))
    ];

    const grouped = chunkGroup(combined, 3); // 3개씩 묶음 (행 기준)

    const settings = {
        slidesToShow: 4,
        slidesToScroll: 4,
        infinite: false,
        arrows: true,
        draggable: false,
        swipe: false,
        nextArrow: <NextArrow />,
        prevArrow: <PrevArrow />,
        responsive: [
            { breakpoint: 1024, settings: { slidesToShow: 3, slidesToScroll: 3} },
            { breakpoint: 768, settings: { slidesToShow: 2, slidesToScroll: 2, arrows: false, draggable: true, swipe: true,} },
            { breakpoint: 480, settings: { slidesToShow: 1, slidesToScroll: 1, arrows: false, draggable: true, swipe: true, } },
        ]
    };


    return (
        <div className="mt-8 container">
            <h2 className="text-xl text-left font-bold my-4">출연 및 제작진</h2>
            <Slider className="relative" {...settings}>
                {grouped.map((chunk, idx) => (
                    <div key={idx}>
                        <div className="flex flex-col gap-4 pr-4">
                            {chunk.map((person) => (
                                <div key={person.name} className="flex gap-4 items-center">
                                    {person.profileImageUrl ? (
                                        <img
                                            src={person.profileImageUrl}
                                            alt={person.name}
                                            className="w-12 h-12 object-cover rounded"
                                        />
                                    ) : (
                                        <div className="w-12 h-12 flex items-center justify-center bg-gray-200 rounded">
                                            <FaUser className="text-gray-400 text-xl" size={30} />
                                        </div>
                                    )}
                                    <div className="h-16 flex-1 flex items-center border-b border-gray-100">
                                        <div className="text-sm text-left space-y-1">
                                            <p className="font-semibold">{person.name}</p>
                                            <p className="text-gray-500 text-xs">
                                                {person.type === "cast"
                                                    ? person.character
                                                        ? `출연 | ${person.character}`
                                                        : "출연"
                                                    : "감독"}
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                ))}
            </Slider>
        </div>
    );
}
