import React, {useEffect, useState} from 'react';
import {FaStar, FaRegStar, FaRegStarHalfStroke} from 'react-icons/fa6';
import {FaRegStarHalf, FaStarHalf} from "react-icons/fa";

export default function StarRatingInput({onRate, initial = 0}) {
    const [hovered, setHovered] = useState(null);
    const [selected, setSelected] = useState(Math.max(initial, 0));

    useEffect(() => {
        if (initial != null && initial !== selected) {
            setSelected(initial);
        }
    }, [initial]);

    const handleClick = (e, index) => {
        const {left, width} = e.currentTarget.getBoundingClientRect();
        const x = e.clientX - left;
        const half = x < width / 2;
        const value = half ? index - 0.5 : index;
        setSelected(value);
        onRate(value);
    };

    const handleMouseMove = (e, index) => {
        const {left, width} = e.currentTarget.getBoundingClientRect();
        const x = e.clientX - left;
        const half = x < width / 2;
        const hoverValue = half ? index - 0.5 : index;
        setHovered(hoverValue);
    };

    const handleMouseLeave = () => {
        setHovered(null);
    };

    const renderStar = (index, size) => {
        const value = hovered !== null ? hovered : selected;

        if (value >= index) {
            // full star
            return <FaStar className="text-yellow-400" size={size}/>;
        } else if (value >= index - 0.5) {
            // half star (left half yellow, right half gray)
            return (
                <div className="relative" style={{width: size, height: size}}>
                    <FaStar className="text-gray-200 absolute top-0 left-0" size={size}/>
                    <div className="absolute top-0 left-0 w-1/2 overflow-hidden">
                        <FaStar className="text-yellow-400" size={size}/>
                    </div>
                </div>
            );
        } else {
            return <FaStar className="text-gray-200" size={size}/>; // 비어 있는 경우 앞에서 렌더링 안 하고 뒤 배경만 보여줌
        }
    };

    return (
        <div className="flex w-full flex-col mb-5 pb-5 border-b border-b-gray-300 md:pb-0 md:border-b-0 md:mb-0 flex-1 items-center lg:items-start">
            <div className="flex cursor-pointer">
                {[1, 2, 3, 4, 5].map((i) => (
                    <div
                        key={i}
                        onClick={(e) => handleClick(e, i)}
                        onMouseMove={(e) => handleMouseMove(e, i)}
                        onMouseLeave={handleMouseLeave}
                        className="pr-0.5 flex items-center justify-center"
                    >
                        {renderStar(i, 42)}
                    </div>
                ))}
            </div>
            <p className="mt-2.5 pl-1 text-left">평가하기</p>
        </div>
    );
}
