//
//  MapViewController.swift
//  PlaceFinder
//
//  Created by Sanginfo on 03/03/17.
//  Copyright © 2017 Sanginfo. All rights reserved.
//

import UIKit
import CoreLocation

class MapViewController: UIViewController,PlaceFinderLocationDelegate{
  var userLocationService: PlaceFinderLocationService?
    override func viewDidLoad() {
        super.viewDidLoad()
      userLocationService = PlaceFinderLocationService()
      userLocationService?.delegate=self
      
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
  // MARK: - Navigation
  func placeFinderLocation(_ placeFinderLocationService: PlaceFinderLocationService, location: CLLocationCoordinate2D) {
    let long = location.longitude;
    let lat = location.latitude;
    print(long, lat)
  }

}
