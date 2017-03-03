//
//  PlaceFinderLocationService.swift
//  PlaceFinder
//
//  Created by Sanginfo on 03/03/17.
//  Copyright © 2017 Sanginfo. All rights reserved.
//

import UIKit
import CoreLocation

protocol PlaceFinderLocationDelegate{
  func placeFinderLocation(_ placeFinderLocationService:PlaceFinderLocationService,location:CLLocationCoordinate2D)
}

class PlaceFinderLocationService: NSObject, CLLocationManagerDelegate {
  var locationManager = CLLocationManager()
  var lastLocation:CLLocationCoordinate2D?
  var delegate:PlaceFinderLocationDelegate! = nil
  
  public override init() {
    super.init()
    lastLocation=kCLLocationCoordinate2DInvalid
    if CLLocationManager.locationServicesEnabled() {
      let locationAuthorizationStatus=CLLocationManager.authorizationStatus()
      if  (locationAuthorizationStatus == .notDetermined ) {
        locationManager.requestAlwaysAuthorization()
        locationManager.requestWhenInUseAuthorization()
        
      }
      else if locationAuthorizationStatus == .denied{
        //Open Setting box to allow user to enable location service
        //TODO: Will correct message afterword
        let title="Location service disable"
        let message="Please Enable location service from setting"
        
        let alert = UIAlertController(title: title, message: message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Setting", style: .default, handler: { (action) in
          UIApplication.shared.openURL(NSURL(string:UIApplicationOpenSettingsURLString) as! URL)
          
        }))
        alert.addAction(UIAlertAction(title: "Cancel", style: .default, handler: { (action) in
        }))
        UIApplication.shared.keyWindow?.rootViewController?.present(alert, animated: true, completion: nil)
      }
      
    
      locationManager.delegate = self
      locationManager.desiredAccuracy = kCLLocationAccuracyBest
      locationManager.startUpdatingLocation()
    }
  }
  
  func myLocation() -> CLLocationCoordinate2D {
    return lastLocation!
  }
  
  func startService() -> Void {
    locationManager.startUpdatingLocation()
  }
  
  func stopService() -> Void {
    locationManager.stopUpdatingLocation()
  }
  // MARK: - CLLocationManagerDelegate
  func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
    let userLocation:CLLocation = locations[0]
    
    if(delegate != nil){
      delegate.placeFinderLocation(self, location: userLocation.coordinate)
    }
    //Do What ever you want with it
  }

}
