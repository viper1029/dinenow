import React, {Component} from 'react'
import {StyleSheet, Dimensions} from 'react-native'
import {Scene, Router, ActionConst} from 'react-native-router-flux'
import NavBar from './components/NavBar'
import NavigationDrawer from './components/NavigationDrawer'
import Theme from './styles/Theme'
import IntroScreen from './views/IntroScreen'
import SignInScreen from './views/SignInScreen'
import SearchScreen from './views/SearchScreen'
import AboutScreen from './views/AboutScreen'
import AccountScreen from './views/AccountScreen'
import ResultsScreen from './views/ResultsScreen'
import SendFeedbackScreen from './views/SendFeedbackScreen'
import RestaurantDetails from './views/RestaurantDetails'
import ItemScreen from './views/ItemScreen'
import DeliveryAddressScreen from './views/DeliveryAddressScreen'
import CheckoutScreen from './views/CheckoutScreen'
import AddDeliveryAddress from './views/AddDeliveryAddress'
import {DrawerNavBar, BackNavBar, BackToHomeNavBar, BackNavBarNoShadow, DrawerNavBarWithAdd} from './components/NavBars';


class NavigationRouter extends Component {

    render() {
        return (
            <Router>
                <Scene key='root' hideNavBar={true}>

                    <Scene key="drawer" component={NavigationDrawer} tabs={true} open={false} type={ActionConst.RESET} direction="horizontal">
                        <Scene key="main" tabs={true}>
                            <Scene key='search' component={SearchScreen} hideNavBar={false} navBar={() => (<DrawerNavBar title="Search"/>)}/>
                            <Scene key='about' component={AboutScreen} hideNavBar={false} navBar={() => (<DrawerNavBar title="About Us"/>)}/>
                            <Scene key='sendFeedback' component={SendFeedbackScreen} hideNavBar={false} navBar={() => (<DrawerNavBar title="Send Feedback"/>)}/>
                            <Scene key='account' initial component={AccountScreen} hideNavBar={false} navBar={() => (<DrawerNavBar title="Account"/>)}/>
                        </Scene>
                </Scene>
                    <Scene key='address' component={DeliveryAddressScreen} hideNavBar={true}/>
                    <Scene key='addAddress' component={AddDeliveryAddress} hideNavBar={true}/>
                    <Scene key='results' component={ResultsScreen} hideNavBar={false} navBar={() => (<BackToHomeNavBar title="Restaurants"/>)} />
                    <Scene key='restaurantDetails' component={RestaurantDetails} />
                    <Scene key='item' component={ItemScreen} />
                    <Scene key='checkout' initial component={CheckoutScreen} />
                </Scene>
            </Router>
        )
    }
}

const styles = StyleSheet.create({
    navBar: {
        backgroundColor: Theme.toolbarDefaultBg,
        shadowColor: '#000',
        shadowOffset: {width: 0, height: 2},
        shadowOpacity: 0.1,
        shadowRadius: 1.5,
        elevation: 2
    }
})


export default NavigationRouter