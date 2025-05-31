import React, { useState } from 'react';
import { FaStar, FaRegStar, FaRegStarHalfStroke } from 'react-icons/fa6';

export default function StarRatingInput({ onRate }) {
    const [hovered, setHovered] = useState(0);
    const [selected, setSelected] = useState(0);

    const handleClick = (e, index) => {
        const { left, width } = e.currentTarget.getBoundingClientRect();
        const x = e.clientX - left;
        const half = x < width / 2;
        const value = half ? index - 0.5 : index;
        setSelected(value);
        onRate(value);
    };

    const handleMouseMove = (e, index) => {
        const { left, width } = e.currentTarget.getBoundingClientRect();
        const x = e.clientX - left;
        const half = x < width / 2;
        const hoverValue = half ? index - 0.5 : index;
        setHovered(hoverValue);
    };

    const handleMouseLeave = () => {
        setHovered(0);
    };

    const renderStar = (index, size) => {
        const value = hovered || selected;
        if (value >= index) return <FaStar size={size} />;
        if (value >= index - 0.5) return <FaRegStarHalfStroke size={size} />;
        return <FaRegStar size={size} />;
    };

    return (
        <div>
            <div className="grid grid-cols-5 text-yellow-400 text-xl cursor-pointer">
                {[1, 2, 3, 4, 5].map((i) => (
                    <div
                        key={i}
                        onClick={(e) => handleClick(e, i)}
                        onMouseMove={(e) => handleMouseMove(e, i)}
                        onMouseLeave={handleMouseLeave}
                        className="pr-1 flex items-center justify-center"
                    >
                        {renderStar(i, 42)}
                    </div>
                ))}
            </div>
            <p className="mt-2.5 pl-1 text-left">평가하기</p>
        </div>
    );
}
