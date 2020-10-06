# Don't Wreck My House

## Tasks

- Initial - 36 mins
    - X Set up maven dependencies - 1 Minute
    - X Organize packages and classes - 15 Minutes
    - X Create models classes - 20 minutes

- Data (including unit testing) - 12 Hours
    - X DataException class - 1 minute
    - GuestFileRepo - 4 Hours
        - X Read Guest - 1 Hour
        - Stretch
            - Create Guest - 1 Hour
            - Update Guest - 1 Hour
            - Delete Guest - 1 Hour 
    - HostFileRepo - 4 Hours
        - Read Hosts - 1 Hour
        - Stretch
            - Create Host - 1 Hour
            - Update Host - 1 Hour
            - Delete Host - 1 Hour
    - ReservationFileRepo - 4 Hours
        - Read Reservation -  1 Hour
        - Create Reservation - 1 Hour
        - Update Reservation - 1 Hour
        - Delete Reservation - 1 Hour
                
- Domain (including unit testing) (also included validation) - 13 Hours 30 Minutes
    - Setting up FileDouble repositories for testing - 1 Hour
    - Response + Result - 30 minutes    
    - GuestService - 4 Hours   
        - Read Guest - 1 Hour
        - Stretch
            - Create Guest - 1 Hour
            - Update Guest - 1 Hour
            - Delete Guest - 1 Hour     
    - HostService - 4 Hours   
        - Read Hosts - 1 Hour
        - Stretch
            - Create Host - 1 Hour
            - Update Host - 1 Hour
            - Delete Host - 1 Hour
    - ReservationService - 4 Hours
        - Read Reservation - 1 Hour
        - Create Reservation - 1 Hour
        - Update Reservation - 1 Hour
        - Delete Reservation - 1 Hour    
        
- UI - 7 Hours
    - ConsoleIO - 1 Hour
    - View - 2 Hours
    - Controller - 2 Hours
    - User testing and debugging - 2 Hours

## Layout
- data
    - HostRepository
    - HostFileRepository
    - GuestRepository
    - GuestFileRepository
    - ReservationRepository
    - ReservationFileRepository

- domain
    - HostService
    - GuestService
    - ReservationService
    - Response
    - Result

- ui
    - Controller
    - View
    - ConsoleIO
    - MainMenuOption (Enumerator)

- models
    - Host
        - String id (generated)
        - String firstName
        - String lastName
        - String email
        - List<Reservation> reservations
    - Guest
        - String id (generated)
        - String firstName
        - String lastName
        - String email
        - String Phone
        - String State
    - Reservation
        - Int id
        - LocalDate startDate
        - LocalDate endDate
        - Guest guest

- file structure
    - guests.csv
    - hosts.csv
    - reservation_data
        - {host-identifier}.csv