import React from 'react';
import AliceCarousel from 'react-alice-carousel';
import 'react-alice-carousel/lib/alice-carousel.css';
import { mainCarouselData } from './MainCarouselData';

const items = mainCarouselData.map((item, index) => (
    <img key={index} className='cursor-pointer' role='presentation' src={item.image} alt='' />
));
const MainCarousel = () => (
    
    <AliceCarousel
        items={items}
        disableButtonsControls
        autoPlay
        autoPlayInterval={2000}
        infinite
    />
);

export default MainCarousel;