import React, { useEffect } from 'react';
import MainCarousel from '../../components/HomeCarousel/MainCarousel';
import HomeSectionCarousel from '../../components/HomeSectionCarousel/HomeSectionCarousel';
import { mens_vest } from '../../../Data/mens_vest';
import { useDispatch, useSelector } from 'react-redux';
import { findProductsByCategory } from '../../../State/Product/Action';
const HomePage = () => {
    const dispatch = useDispatch();
    useEffect(() => {
        dispatch(findProductsByCategory());
    }, [dispatch]);
    const data = useSelector((store) => store.products.productsByCategory);
    return (
        <div>
            <MainCarousel />
            <div className='space-y-10 py-20 flex flex-col justify-center px-5 lg:px-10'>
                <HomeSectionCarousel data={data.vest} sectionName={"Men's Vest"}/>
                {/* <HomeSectionCarousel data={data.shoes} sectionName={"Men's Shoes"}/> */}
                <HomeSectionCarousel data={data.shirt} sectionName={"Men's Shirt"}/>
                <HomeSectionCarousel data={data.dress} sectionName={"Women's Dress"}/>
                <HomeSectionCarousel data={data.jeans} sectionName={"Women's Jeans"}/>
            </div>

        </div>
    )
}

export default HomePage;